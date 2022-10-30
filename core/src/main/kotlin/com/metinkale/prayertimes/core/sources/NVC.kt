package com.metinkale.prayertimes.core.sources

import com.metinkale.prayertimes.core.DayTimes
import com.metinkale.prayertimes.core.Entry
import com.metinkale.prayertimes.core.client
import com.metinkale.prayertimes.core.sources.features.SearchFeature
import com.metinkale.prayertimes.core.utils.now
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

object NVC : Source("NVC"), SearchFeature {

    override suspend fun getDayTimes(id: String): List<DayTimes> {
        val year = LocalDate.now().year
        return client.get("https://namazvakti.com/XML.php?cityID=$id").bodyAsText().lines()
            .filter { "<prayertimes" in it }
            .map {
                val date = it.split("\"").let {
                    LocalDate(year, it[5].toInt(), it[3].toInt())
                }

                val times = it.split(">")[1].split("<")[0].split("\t").map { it.padStart(5 - it.length, '0') }


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



    override fun getCities(): Sequence<Entry> = readTSV(this,"/tsv/nvc.tsv")


    // toLocalTime() seems not to work so we do it here
    private fun String.parseTime() = split(":").let { LocalTime(it[0].toInt(), it[1].toInt()) }
}
