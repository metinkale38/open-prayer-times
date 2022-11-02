package dev.metinkale.prayertimes.core

@JvmInline
value class CountryCode private constructor(val code: String) {
    enum class Lang { en, tr, de }

    operator fun get(lang: dev.metinkale.prayertimes.core.CountryCode.Lang) = dev.metinkale.prayertimes.core.CountryCode.Companion.readTSV(
        lang
    ).find { it.first.equals(code, false) }?.second

    companion object {

        fun find(name: String): dev.metinkale.prayertimes.core.CountryCode? {

            return when (name) {
                "Bosnia-Herzegovina" -> "BA"
                "Butan" -> "BT"
                "Cad" -> "TD"
                "Chechenia" -> "RU"
                "Czech Republic" -> "CZ"
                "Demokratic Republic Of The Congo" -> "CD"
                "Dogu Timor" -> "TL"
                "Fildisi Sahili" -> "CI"
                "Gronland" -> "GL"
                "Guadelope" -> "GP"
                "Guam Island" -> "GU"
                "South Korea" -> "KR"
                "Hirvatistan" -> "HR"
                "Hollanda Antilleri" -> "AN"
                "Izlanda" -> "IS"
                "Kambocya" -> "KH"
                "Karadag" -> "ME"
                "Kirgizhstan" -> "KG"
                "Kostarika" -> "CR"
                "Kudus" -> "PS"
                "North Cyprus" -> "CY"
                "North Korea" -> "KP"
                "Martinik" -> "MQ"
                "Mauritius Adasi" -> "MU"
                "Moldavya" -> "MD"
                "Montserrat (U.K.)" -> "MS"
                "Pitcairn Adasi" -> "PN"
                "Seysel Adalari" -> "SC"
                "Sirbistan" -> "RS"
                "Trinidat ve Tobago" -> "TT"
                "Tunusia" -> "TN"
                "Ukraine-Krym" -> "UA"
                "Yesil Burun" -> "CV"
                "Man Island" -> "IM"
                else -> null
            }?.let { dev.metinkale.prayertimes.core.CountryCode(it) } ?: dev.metinkale.prayertimes.core.CountryCode.Lang.values().firstNotNullOfOrNull {
                dev.metinkale.prayertimes.core.CountryCode.Companion.find(
                    it,
                    name
                )
            }
        }

        fun find(lang: dev.metinkale.prayertimes.core.CountryCode.Lang, name: String): dev.metinkale.prayertimes.core.CountryCode? =
            dev.metinkale.prayertimes.core.CountryCode.Companion.readTSV(lang)
                .find { it.second == name }?.let { dev.metinkale.prayertimes.core.CountryCode(it.first) }
                ?: dev.metinkale.prayertimes.core.CountryCode.Companion.readTSV(lang).find { it.second.contains(name) }?.let {
                    dev.metinkale.prayertimes.core.CountryCode(it.first)
                }


        private fun readTSV(lang: dev.metinkale.prayertimes.core.CountryCode.Lang): Sequence<Pair<String, String>> {
            return dev.metinkale.prayertimes.core.CountryCode::class.java.getResourceAsStream("/countrycodes/${lang.name}.tsv")!!
                .bufferedReader(Charsets.UTF_8)
                .lineSequence().map { it.split("\t").let { it[0] to it[1] } }
        }
    }
}

