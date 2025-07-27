package dev.metinkale.hijri

import kotlinx.datetime.LocalDate

internal interface IPlatform {

    fun convertFromHijri(date: HijriDate): LocalDate

    fun convertToHijri(date: LocalDate): HijriDate


    fun readFile(path: String): Sequence<String>
}

internal expect val Platform: IPlatform