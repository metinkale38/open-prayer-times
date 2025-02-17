package dev.metinkale.prayertimes.providers

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class DayTimes(
    /** Date */
    val date: LocalDate,
    /** Time for fajr prayer */
    val fajr: LocalTime,
    /** Sunrise */
    val sun: LocalTime,
    /** Time for Dhuhr Prayer */
    val dhuhr: LocalTime,
    /** Time for Asr Prayer */
    val asr: LocalTime,
    /** Time for Maghrib Prayer */
    val maghrib: LocalTime,
    /** Time for Ishaa Prayer */
    val ishaa: LocalTime,
    /** OPTIONAL: asr according to Hanafi () */
    val asrHanafi: LocalTime? = null,
    /** OPTIONAL: slightly after fajr */
    val sabah: LocalTime? = null,
)