package dev.metinkale.prayertimes.dto

import com.metinkale.prayer.date.HijriEvent
import dev.metinkale.hijri.HijriDate
import dev.metinkale.prayertimes.providers.DayTimes
import kotlinx.datetime.toJavaLocalDate
import kotlinx.serialization.Serializable

@Serializable
data class DayTimesDTO(
    val date: String,
    val hijri: String,
    val event: HijriEvent?,
    val fajr: String,
    val sun: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val ishaa: String,
    val asrHanafi: String? = null,
    val sabah: String? = null,
) {
    companion object {
        fun from(daytimes: DayTimes): DayTimesDTO {
            val hijri = HijriDate.fromLocalDate(daytimes.date.toJavaLocalDate())
            return DayTimesDTO(
                date = daytimes.date.toString(),
                hijri = HijriDate.fromLocalDate(daytimes.date.toJavaLocalDate()).toString(),
                event = HijriDate.getEventsForHijriYear(hijri.year)
                    .find { it.first == hijri && it.second != HijriEvent.MONTH }?.second,
                fajr = daytimes.fajr.toString(),
                sun = daytimes.sun.toString(),
                dhuhr = daytimes.dhuhr.toString(),
                asr = daytimes.asr.toString(),
                maghrib = daytimes.maghrib.toString(),
                ishaa = daytimes.ishaa.toString()
            )
        }
    }
}