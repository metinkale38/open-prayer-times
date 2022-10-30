package com.metinkale.prayertimes.core

@JvmInline
value class CountryCode private constructor(val code: String) {
    enum class Lang { en, tr, de }

    operator fun get(lang: Lang) = readTSV(lang).find { it.first.equals(code, false) }?.second

    companion object {

        fun find(name: String): CountryCode? {

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
            }?.let { CountryCode(it) } ?: Lang.values().firstNotNullOfOrNull { find(it, name) }
        }

        fun find(lang: Lang, name: String): CountryCode? =
            readTSV(lang).find { it.second == name }?.let { CountryCode(it.first) }
                ?: readTSV(lang).find { it.second.contains(name) }?.let { CountryCode(it.first) }


        private fun readTSV(lang: Lang): Sequence<Pair<String, String>> {
            return CountryCode::class.java.getResourceAsStream("/countrycodes/${lang.name}.tsv")!!
                .bufferedReader(Charsets.UTF_8)
                .lineSequence().map { it.split("\t").let { it[0] to it[1] } }
        }
    }
}

