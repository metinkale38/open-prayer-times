package dev.metinkale.openprayertimes

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
    /**
     * Indicates whether [sabah] is calculated via estimated rules (Takdir)
     * because the actual astronomical time is unreachable or occurs too early
     * (e.g., in high latitudes during summer).
     *
     * Not supported in all calendars
     */
    val isSabahEstimated: Boolean = false,
    /**
     * Indicates whether [fajr] is calculated via estimated rules (Takdir)
     * because the actual astronomical time is unreachable or occurs too early
     * (e.g., in high latitudes during summer).
     *
     * Not supported in all calendars
     */
    val isFajrEstimated: Boolean = false,
    /**
     * Indicates whether [fajr] is calculated via estimated rules (Takdir)
     * because the actual astronomical time is unreachable or occurs too late
     * (e.g., in high latitudes during summer).
     *
     * Not supported in all calendars
     */
    val isIshaaEstimated: Boolean = false,
)