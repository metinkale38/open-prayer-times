package dev.metinkale.prayertimes.core.sources

import dev.metinkale.prayertimes.core.DayTimes
import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.HttpClient
import dev.metinkale.prayertimes.core.Configuration
import dev.metinkale.prayertimes.core.sources.features.CityListFeature
import dev.metinkale.prayertimes.core.sources.features.DayTimesFeature
import dev.metinkale.prayertimes.core.utils.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal object London : Source, CityListFeature {

    override val name: String = "London"
    override val fullName: String ="LondonPrayerTimes.com"
        private
    val json = Json { ignoreUnknownKeys = true }

    override suspend fun getDayTimes(key: String): List<DayTimes> {
        val response =
            HttpClient.get("https://www.londonprayertimes.com/api/times/?format=json&key=${Configuration.LONDON_PRAYER_TIMES_API_KEY}&year=" + LocalDate.now().year)
                .let {
                    json.decodeFromString(Result.serializer(), it)
                }


        return response.times?.values?.map {
            val date = it.date!!.toLocalDate()
            val times = mutableListOf(
                it.fajr!!.toLocalTime(),
                it.sunrise!!.toLocalTime(),
                it.dhuhr!!.toLocalTime(),
                it.asr!!.toLocalTime(),
                it.asr_2!!.toLocalTime(),
                it.magrib!!.toLocalTime(),
                it.isha!!.toLocalTime(),
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

    override fun getCities(): Sequence<Entry> = sequenceOf(
        Entry(
            id = "0",
            lat = 51.5073219,
            lng = -0.1276473,
            country = "EN",
            names = listOf(mapOf("" to "London")),
            source = London
        )
    )

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
