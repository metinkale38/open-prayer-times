package dev.metinkale.calctimes

import kotlinx.datetime.LocalTime


data class PrayerTimes(

    /** The time to stop eating Sahur (for fasting), slightly before Fajr. */
    val imsak: LocalTime,

    /** When the sky begins to lighten (dawn). */
    val fajr: LocalTime,

    /** The time at which the first part of the Sun appears above the horizon. */
    val sunrise: LocalTime,

    /** Zawal (Solar Noon): When the Sun reaches its highest point in the sky. */
    val zawal: LocalTime,

    /** When the Sun begins to decline after reaching its highest point in the sky, slightly after solar noon */
    val dhuhr: LocalTime,

    /** The time of Asr (Shafi'i), when shadow length = object length + noon shadow length. */
    val asrShafi: LocalTime,

    /** The time of Asr (Hanafi), when shadow length = (2 * object length) + noon shadow length. */
    val asrHanafi: LocalTime,

    /** The time at which the Sun disappears below the horizon. */
    val sunset: LocalTime,

    /** Soon after sunset. */
    val maghrib: LocalTime,

    /** The time at which darkness falls and there is no scattered light in the sky. */
    val ishaa: LocalTime,

    /** First third of the night */
    val firstThird: LocalTime,

    /** The mean time from sunset to sunrise */
    val midnight: LocalTime,

    /** The last third of the night */
    val lastThird: LocalTime,

    /**
     * whether high lats adjustment was used for fajr or not
     */
    val usedHighLatsAdjustmentFajr: Boolean,

    /**
     * whether high lats adjustment was used for imsak or not
     */
    val usedHighLatsAdjustmentImsak: Boolean,

    /**
     * whether high lats adjustment was used for maghrib or not
     */
    val usedHighLatsAdjustmentMaghrib: Boolean,

    /**
     * whether high lats adjustment was used for ihsaa or not
     */
    val usedHighLatsAdjustmentIshaa: Boolean
)
