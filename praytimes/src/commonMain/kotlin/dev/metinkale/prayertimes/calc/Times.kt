package dev.metinkale.prayertimes.calc


data class Times<T>(

    /** The time to stop eating Sahur (for fasting), slightly before Fajr. */
    var imsak: T,

    /** When the sky begins to lighten (dawn). */
    var fajr: T,

    /** The time at which the first part of the Sun appears above the horizon. */
    var sunrise: T,

    /** Zawal (Solar Noon): When the Sun reaches its highest point in the sky. */
    var zawal: T,

    /** When the Sun begins to decline after reaching its highest point in the sky, slightly after solar noon */
    var dhuhr: T,

    /** The time when the length of any object's shadow reaches a factor 1 of the length of the object itself plus the length of that object's shadow at noon */
    var asrShafi: T,

    /** The time when the length of any object's shadow reaches a factor 2 of the length of the object itself plus the length of that object's shadow at noon */
    var asrHanafi: T,

    /** The time at which the Sun disappears below the horizon. */
    var sunset: T,

    /** Soon after sunset. */
    var maghrib: T,

    /** The time at which darkness falls and there is no scattered light in the sky. */
    var ishaa: T,

    /** The mean time from sunset to sunrise */
    var midnight: T
) {
    companion object {

        fun <T> properties() = listOf(
            Times<T>::imsak,
            Times<T>::fajr,
            Times<T>::sunrise,
            Times<T>::zawal,
            Times<T>::dhuhr,
            Times<T>::asrShafi,
            Times<T>::asrHanafi,
            Times<T>::sunset,
            Times<T>::maghrib,
            Times<T>::ishaa,
            Times<T>::midnight
        )

        fun <T> withDefault(default: T) =
            Times(default, default, default, default, default, default, default, default, default, default, default)

    }
}
