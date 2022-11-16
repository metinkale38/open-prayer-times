package dev.metinkale.prayertimes.calc


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
     * [AngleBased][HighLatsAdjustment.AngleBased]
     */
    val highLats: HighLatsAdjustment = HighLatsAdjustment.None,

    /**
     * Midnight is generally calculated as the mean time from Sunset to Sunrise, i.e., Midnight = 1/2(Sunrise - Sunset).
     * In Shia point of view, the juridical midnight (the ending time for performing Isha prayer) is the mean time
     * from Sunset to Fajr, i.e., Midnight = 1/2(Fajr - Sunset).
     *
     *
     * [Standard][Midnight.Standard] (Default)
     * [Jafari][Midnight.Jafari]
     */
    val midnight: Midnight = Midnight.Standard,

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
            listOf(MWL, ISNA, Egypt, Makkah, Karachi, UOIF, Tehran, Jafari)

        /**
         * Muslim World League
         * Fajr: 18*, Maghrib: Sunset, Ishaa: 17*
         */
        val MWL = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.Standard,
            imsakAngle = 18.0,
            fajrAngle = 18.0,
            ishaaAngle = 17.0
        ).apply { serializeTo = "MWL" }

        /**
         * Islamic Society of North America (ISNA)
         * Fajr: 15*, Maghrib: Sunset, Ishaa: 15*
         */
        val ISNA = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.Standard,
            fajrAngle = 18.0,
            imsakAngle = 18.0,
            ishaaAngle = 15.0
        ).apply { serializeTo = "ISNA" }

        /**
         * Egyptian General Authority of Survey
         * Fajr: 19.5*, Maghrib: Sunset, Ishaa: 17.5*
         */
        val Egypt = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.Standard,
            fajrAngle = 19.5,
            imsakAngle = 19.5,
            ishaaAngle = 17.5
        ).apply { serializeTo = "Egypt" }

        /**
         * Umm Al-Qura University, Makkah
         * Fajr: 18.5*, Maghrib: Sunset, Ishaa: 90min after sunset
         */
        val Makkah = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.Standard,
            fajrAngle = 18.5,
            imsakAngle = 18.5,
            ishaaMinute = 90
        ).apply { serializeTo = "Makkah" }


        /**
         * University of Islamic Sciences, Karachi
         * Fajr: 18*, Maghrib: Sunset, Ishaa: 15*
         */
        val Karachi = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.Standard,
            fajrAngle = 18.0,
            imsakAngle = 18.0,
            ishaaAngle = 15.0
        ).apply { serializeTo = "Karachi" }

        /**
         * Union des organisations islamiques de France
         * Fajr: 12*, Maghrib: Sunset, Ishaa: 12*
         */
        val UOIF = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.Standard,
            fajrAngle = 12.0,
            imsakAngle = 12.0,
            ishaaAngle = 12.0
        ).apply { serializeTo = "UOIF" }

        /**
         * Institute of Geophysics, University of Tehran
         * Fajr: 17.7*, Maghrib: 4.5*, Ishaa: 14*
         */
        val Tehran = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.Standard,
            fajrAngle = 17.7,
            imsakAngle = 17.7,
            maghribAngle = 4.5,
            ishaaAngle = 14.0
        ).apply { serializeTo = "Tehran" }


        /**
         * Shia Ithna-Ashari, Leva Institute, Qum
         * Fajr: 16*, Maghrib: 4*, Ishaa: 14*
         */
        val Jafari = Method(
            highLats = HighLatsAdjustment.None,
            midnight = Midnight.Standard,
            fajrAngle = 16.0,
            imsakAngle = 16.0,
            maghribAngle = 4.0,
            ishaaAngle = 14.0
        ).apply { serializeTo = "Jafari" }



        fun deserialize(value: String): MethodBuilder = when (value) {
            "MWL" -> MWL
            "ISNA" -> ISNA
            "Egypt" -> Egypt
            "Makkah" -> Makkah
            "Karachi" -> Karachi
            "UOIF" -> UOIF
            "Tehran" -> Tehran
            "Jafari" -> Jafari
            else -> value.split(";").let {
                Method(
                    highLats = HighLatsAdjustment.valueOf(it[0]),
                    midnight = Midnight.valueOf(it[1]),
                    imsakAngle = it[2].toDoubleOrNull(),
                    imsakMinute = it[3].toIntOrNull() ?: 0,
                    fajrAngle = it[4].toDoubleOrNull(),
                    fajrMinute = it[5].toIntOrNull() ?: 0,
                    sunriseMinute = it[6].toIntOrNull() ?: 0,
                    dhuhrMinute = it[7].toIntOrNull() ?: 0,
                    asrShafiMinute = it[8].toIntOrNull() ?: 0,
                    asrHanafiMinute = it[9].toIntOrNull() ?: 0,
                    sunsetMinutes = it[10].toIntOrNull() ?: 0,
                    maghribAngle = it[11].toDoubleOrNull(),
                    maghribMinute = it[12].toIntOrNull() ?: 0,
                    ishaaAngle = it[13].toDoubleOrNull(),
                    ishaaMinute = it[14].toIntOrNull() ?: 0
                )
            }
        }

    }

    private var serializeTo: String? = null

    val shortName get() = serializeTo ?: "Custom"

    val name
        get() = when (serializeTo) {
            "MWL" -> "Muslim World League"
            "ISNA" -> "Islamic Society of North America (ISNA)"
            "Egypt" -> "Egyptian General Authority of Survey"
            "Makkah" -> "Umm Al-Qura University, Makkah"
            "Karachi" -> "University of Islamic Sciences, Karachi"
            "UOIF" -> "Union des organisations islamiques de France"
            "Tehran" -> "Institute of Geophysics, University of Tehran"
            "Jafari" -> "Shia Ithna-Ashari, Leva Institute, Qum"
            "IGMG" -> "Islamische Gemeinschaft Millî Görüş"
            else -> "Custom"
        }

    fun serialize(): String = serializeTo ?: listOf(
        highLats.name,
        midnight.name,
        imsakAngle?.toString(),
        imsakMinute.takeIf { it != 0 }?.toString(),
        fajrAngle?.toString(),
        fajrMinute.takeIf { it != 0 }?.toString(),
        sunriseMinute.takeIf { it != 0 }?.toString(),
        dhuhrMinute.takeIf { it != 0 }?.toString(),
        asrShafiMinute.takeIf { it != 0 }?.toString(),
        asrHanafiMinute.takeIf { it != 0 }?.toString(),
        sunsetMinutes.takeIf { it != 0 }?.toString(),
        maghribAngle?.toString(),
        maghribMinute.takeIf { it != 0 }?.toString(),
        ishaaAngle?.toString(),
        ishaaMinute.takeIf { it != 0 }?.toString()
    ).map {
        it.orEmpty()
    }.joinToString(";").also { serializeTo = it }

}


fun interface MethodBuilder {
    fun build(latitude: Double, longitude: Double, elevation: Double): Method
}
