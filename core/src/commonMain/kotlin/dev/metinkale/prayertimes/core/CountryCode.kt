package dev.metinkale.prayertimes.core

import kotlin.jvm.JvmInline

@JvmInline
value class CountryCode private constructor(val code: String) {
    enum class Lang { en, tr, de }

    operator fun get(lang: Lang) = readTSV(
        lang
    ).find { it.first.equals(code, false) }?.second

    companion object {

        fun find(name: String): CountryCode? {
            return Lang.values().firstNotNullOfOrNull { find(it, name) }
        }

        fun find(lang: Lang, name: String): CountryCode? =
            readTSV(lang)
                .find { it.second == name }?.let { CountryCode(it.first) }
                ?: readTSV(lang).find { it.second.contains(name) }?.let {
                    CountryCode(it.first)
                }


        private fun readTSV(lang: Lang): Sequence<Pair<String, String>> {
            return readFileAsLineSequence("/countrycodes/${lang.name}.tsv").map {
                it.split("\t").let { it[0] to it[1] }
            }
        }
    }
}

