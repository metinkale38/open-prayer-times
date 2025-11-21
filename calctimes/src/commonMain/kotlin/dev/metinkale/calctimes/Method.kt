package dev.metinkale.calctimes

data class Method(
    /**
     * In locations at higher latitude, twilight may persist throughout the night during some months of the year.
     * In these abnormal periods, the determination of Fajr and Isha is not possible using the usual formulas mentioned
     * in the previous section. To overcome this problem, several solutions have been proposed,
     * three of which are described below.
     *
     * [None][HighLatsAdjustment.None] (Default, see notes)
     * [NightMiddle][HighLatsAdjustment.NightMiddle]
     * [OneSeventh][HighLatsAdjustment.OneSeventh]
     * [OneSeventh][HighLatsAdjustment.OneThird]
     * [OneSeventh][HighLatsAdjustment.OneFifth]
     * [AngleBased][HighLatsAdjustment.AngleBased]
     */
    val highLats: HighLatsAdjustment = HighLatsAdjustment.None,
    /**
     * The highLatsType decides, when to use the Adjustment or Higher Latitude. You can either use it as maximum time
     * for ishaa (1/7, 1/3 of night etc.) or only if ishaa cannot be calculated
     */
    val highLatsType: HighLatsType = HighLatsType.Max,

    /**
     * Midnight is generally calculated as the mean time from Sunset to Sunrise, i.e., Midnight = 1/2(Sunrise - Sunset).
     * In Shia point of view, the juridical midnight (the ending time for performing Isha prayer) is the mean time
     * from Sunset to Fajr, i.e., Midnight = 1/2(Fajr - Sunset).
     *
     *
     * [SunsetFajr][Midnight.SunsetFajr] (Default)
     * [SunsetSunrise][Midnight.SunsetSunrise]
     */
    val midnight: Midnight = Midnight.SunsetFajr,

    /** Imsak: if angle is null, sunrise + minute is used, given angle + minute otherwise */
    val imsakAngle: Double? = null,
    /** Imsak: if angle is null, sunrise + minute is used, given angle + minute otherwise */
    val imsakMinute: Int = 0,
    /** Fajr: if angle is null, sunrise + minute is used, given angle + minute otherwise */
    val fajrAngle: Double? = null,
    /** Fajr: if angle is null, sunrise + minute is used, given angle + minute otherwise */
    val fajrMinute: Int = 0,
    /** Sunrise: sunrise has a fix calculation, we can only shift minutes */
    val sunriseMinute: Int = 0,
    /** Dhuhr: dhuhr has a fix calculation (see Zawal), we can only shift minutes */
    val dhuhrMinute: Int = 0,
    /** AsrShafi: Asr has a fix calculation, we can only shift minutes */
    val asrShafiMinute: Int = 0,
    /** AsrHanafi: Asr has a fix calculation, we can only shift minutes */
    val asrHanafiMinute: Int = 0,
    /** Sunset: sunset has a fix calculation, we can only shift minutes */
    val sunsetMinutes: Int = 0,
    /** Maghrib: if angle is null, sunset + minute is used, given angle + minute otherwise */
    val maghribAngle: Double? = null,
    /** Maghrib: if angle is null, sunset + minute is used, given angle + minute otherwise */
    val maghribMinute: Int = 0,
    /** Ishaa: if angle is null, sunset + minute is used, given angle + minute otherwise */
    val ishaaAngle: Double? = null,
    /** Ishaa: if angle is null, sunset + minute is used, given angle + minute otherwise */
    val ishaaMinute: Int = 0,
    /** for more accuracy we use the elevation for more accuracy, but some methods might disable it to match the original sources */
    val useElevation: Boolean = true

) : MethodBuilder {

    override fun build(latitude: Double, longitude: Double, elevation: Double): Method = this

    companion object {

        fun values(): List<MethodBuilder> =
            listOf(MWL, ISNA, Egypt, Makkah, Karachi, UOIF, Russia, Singapore, Tehran, Jafari)

        /**
         * Muslim World League
         * Fajr: 18*, Maghrib: Sunset, Ishaa: 17*
         */
        val MWL = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.SunsetSunrise,
            imsakAngle = 18.0,
            fajrAngle = 18.0,
            ishaaAngle = 17.0
        ).apply {
            shortName = "MWL"
            name = "Muslim World League"
        }

        /**
         * Islamic Society of North America (ISNA)
         * Fajr: 15*, Maghrib: Sunset, Ishaa: 15*
         */
        val ISNA = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.SunsetSunrise,
            fajrAngle = 18.0,
            imsakAngle = 18.0,
            ishaaAngle = 15.0
        ).apply {
            shortName = "ISNA"
            name = "Islamic Society of North America (ISNA)"
        }

        /**
         * Egyptian General Authority of Survey
         * Fajr: 19.5*, Maghrib: Sunset, Ishaa: 17.5*
         */
        val Egypt = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.SunsetSunrise,
            fajrAngle = 19.5,
            imsakAngle = 19.5,
            ishaaAngle = 17.5
        ).apply {
            shortName = "Egypt"
            name = "Egyptian General Authority of Survey"
        }

        /**
         * Umm Al-Qura University, Makkah
         * Fajr: 18.5*, Maghrib: Sunset, Ishaa: 90min after sunset
         */
        val Makkah = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.SunsetSunrise,
            fajrAngle = 18.5,
            imsakAngle = 18.5,
            ishaaMinute = 90
        ).apply {
            shortName = "Makkah"
            name = "Umm Al-Qura University, Makkah"
        }


        /**
         * University of Islamic Sciences, Karachi
         * Fajr: 18*, Maghrib: Sunset, Ishaa: 15*
         */
        val Karachi = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.SunsetSunrise,
            fajrAngle = 18.0,
            imsakAngle = 18.0,
            ishaaAngle = 15.0
        ).apply {
            shortName = "Karachi"
            name = "University of Islamic Sciences, Karachi"
        }

        /**
         * Union des organisations islamiques de France
         * Fajr: 12*, Maghrib: Sunset, Ishaa: 12*
         */
        val UOIF = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.SunsetSunrise,
            fajrAngle = 12.0,
            imsakAngle = 12.0,
            ishaaAngle = 12.0
        ).apply {
            shortName = "UOIF"
            name = "Union des organisations islamiques de France"
        }

        /**
         * Spiritual Administration of Muslims of Russia
         */
        val Russia = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.SunsetSunrise,
            fajrAngle = 16.0,
            imsakAngle = 16.0,
            maghribAngle = 0.0,
            ishaaAngle = 15.0
        ).apply {
            shortName = "Russia"
            name = "Spiritual Administration of Muslims of Russia"
        }

        /**
         * Islamic Religious Council of Singapore
         */
        val Singapore = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.SunsetSunrise,
            fajrAngle = 20.0,
            imsakAngle = 20.0,
            maghribAngle = 0.0,
            ishaaAngle = 18.0
        ).apply {
            shortName = "Singapore"
            name = "Islamic Religious Council of Singapore"
        }


        /**
         * Presidency of Religious Affairs, Turkey
         * Fajr: 18*, Maghrib: 18*, Ishaa: 17*
         */
        val Turkey = Method(
            highLats = HighLatsAdjustment.OneThird,
            midnight = Midnight.SunsetSunrise,
            fajrAngle = 18.0,
            imsakAngle = 18.0,
            dhuhrMinute = 6,
            asrShafiMinute = 3,
            asrHanafiMinute = 3,
            maghribMinute = 3,
            ishaaAngle = 17.0,
            ishaaMinute = 3
        ).apply {
            shortName = "Turkey"
            name = "Presidency of Religious Affairs, Turkey"
        }

        /**
         * Institute of Geophysics, University of Tehran
         * Fajr: 17.7*, Maghrib: 4.5*, Ishaa: 14*
         */
        val Tehran = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.SunsetSunrise,
            fajrAngle = 17.7,
            imsakAngle = 17.7,
            maghribAngle = 4.5,
            ishaaAngle = 14.0
        ).apply {
            shortName = "Tehran"
            name = "Institute of Geophysics, University of Tehran"
        }


        /**
         * Shia Ithna-Ashari, Leva Institute, Qum
         * Fajr: 16*, Maghrib: 4*, Ishaa: 14*
         */
        val Jafari = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.SunsetSunrise,
            fajrAngle = 16.0,
            imsakAngle = 16.0,
            maghribAngle = 4.0,
            ishaaAngle = 14.0
        ).apply {
            shortName = "Jafari"
            name = "Shia Ithna-Ashari, Leva Institute, Qum"
        }


    }

    override var shortName: String? = null
        private set
    override var name: String? = null
        private set
}


fun interface MethodBuilder {
    fun build(latitude: Double, longitude: Double, elevation: Double): Method
    val shortName: String? get() = null
    val name: String? get() = null
}
