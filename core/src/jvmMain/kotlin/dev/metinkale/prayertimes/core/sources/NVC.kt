package dev.metinkale.prayertimes.core.sources

import dev.metinkale.prayertimes.core.DayTimes
import dev.metinkale.prayertimes.core.httpClient
import dev.metinkale.prayertimes.core.sources.features.CityListFeature
import dev.metinkale.prayertimes.core.utils.now
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

internal object NVC : Source, CityListFeature {

    override val name: String = "NVC"
    override val fullName: String = "NamazVakti.com"

    override suspend fun getDayTimes(key: String): List<DayTimes> {
        val year = LocalDate.now().year
        return httpClient.get("https://namazvakti.com/XML.php?cityID=$key").bodyAsText().lines()
            .filter { "<prayertimes" in it }
            .map {
                val date = it.split("\"").let {
                    LocalDate(year, it[5].toInt(), it[3].toInt())
                }

                val times = it.split(">")[1].split("<")[0].split("\t")
                    .map { it.padStart(5 - it.length, '0') }


                DayTimes(
                    date = date,
                    fajr = times[0].parseTime(),
                    sabah = times[1].parseTime(),
                    sun = times[2].parseTime(),
                    // Ishrak
                    // Karahat
                    dhuhr = times[5].parseTime(),
                    asr = times[6].parseTime(),
                    asrHanafi = times[7].parseTime(),
                    // isfirar
                    maghrib = times[9].parseTime(),
                    // ishtibak
                    ishaa = times[11].parseTime()
                    // ishaa thani
                    // Qibla Time
                )
            }
    }


    // toLocalTime() seems not to work so we do it here
    private fun String.parseTime() =
        replace("*", "").split(":").let { LocalTime(it[0].toInt(), it[1].toInt()) }
}
