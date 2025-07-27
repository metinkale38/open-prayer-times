package dev.metinkale.prayertimes.providers.sources

import dev.metinkale.prayertimes.providers.*
import dev.metinkale.prayertimes.providers.geo.Geolocation
import dev.metinkale.prayertimes.providers.sources.features.IterableSource
import dev.metinkale.prayertimes.providers.utils.now
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.math.abs

internal object London : Source, IterableSource {

    override val name: String = "London"
    override val fullName: String = "LondonPrayerTimes.com"

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getDayTimes(key: String): List<DayTimes> {
        val response =
            httpClient.get("https://www.londonprayertimes.com/api/times/?format=json&key=${Configuration.LONDON_PRAYER_TIMES_API_KEY}&year=" + LocalDate.now().year)
                .bodyAsText()
                .let { json.decodeFromString(Result.serializer(), it) }


        return response.times?.values?.map {
            val date = LocalDate.parse(it.date!!)
            val times = mutableListOf(
                LocalTime.parse(it.fajr!!),
                LocalTime.parse(it.sunrise!!),
                LocalTime.parse(it.dhuhr!!),
                LocalTime.parse(it.asr!!),
                LocalTime.parse(it.asr_2!!),
                LocalTime.parse(it.magrib!!),
                LocalTime.parse(it.isha!!),
            )

            var last: LocalTime? = null
            times.forEachIndexed { index, time ->
                if (last == null) last = time
                else if (last!! > time) times[index] = LocalTime(time.hour + 12, time.minute)
            }

            DayTimes(
                date,
                fajr = times[0],
                sun = times[1],
                dhuhr = times[2],
                asr = times[3],
                asrHanafi = times[4],
                maghrib = times[5],
                ishaa = times[6]
            )
        } ?: emptyList()
    }


    private val entry = Entry(
        id = "0",
        lat = 51.5073219,
        lng = -0.1276473,
        country = "EN",
        names = listOf(mapOf("" to "London")),
        source = London
    )

    override suspend fun search(query: String, location: Geolocation?): Entry? =
        entry.takeIf { "London".contains(query, true) || location?.name == "London" }


    override suspend fun search(geolocation: Geolocation): Entry? =
        entry.takeIf { abs(geolocation.lat - entry.lat!!) < 2 && abs(geolocation.lng - entry.lng!!) < 2 }


    override suspend fun list(path: List<String>, languages: List<String>): Pair<List<String>?, Entry?> {
        return null to entry
    }

    override val supported: Boolean
        get() = Configuration.LONDON_PRAYER_TIMES_API_KEY.isNotEmpty()

    @Serializable
    private data class Result(
        val times: Map<String, Times>? = null
    )

    @Serializable
    private data class Times(
        val date: String? = null,
        val fajr: String? = null,
        val sunrise: String? = null,
        val dhuhr: String? = null,
        val asr: String? = null,
        val asr_2: String? = null,
        val magrib: String? = null,
        val isha: String? = null
    )
}

val Source.Companion.London: IterableSource get() = dev.metinkale.prayertimes.providers.sources.London