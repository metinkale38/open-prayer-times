package dev.metinkale.openprayertimes.hijri

import kotlinx.datetime.*
import java.io.FileNotFoundException
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.chrono.HijrahEra
import java.time.temporal.ChronoField
import kotlin.time.Clock

internal fun LocalDate.previousOrSame(targetDay: DayOfWeek): LocalDate {
    val daysToSubtract = (this.dayOfWeek.isoDayNumber - targetDay.isoDayNumber + 7) % 7
    return this.minus(daysToSubtract.toLong(), DateTimeUnit.DAY)
}


fun LocalDate.Companion.now(): LocalDate =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun convertFromHijri(date: HijriDate): LocalDate {
    return java.time.LocalDate.from(HijrahChronology.INSTANCE.date(HijrahEra.AH, date.year, date.monthValue, date.day))
        .toKotlinLocalDate()
}

fun convertToHijri(date: LocalDate): HijriDate {
    val hijrahDate: HijrahDate = HijrahChronology.INSTANCE.date(date.toJavaLocalDate())
    return HijriDate.of(
        hijrahDate.getLong(ChronoField.YEAR_OF_ERA).toInt(),
        hijrahDate.getLong(ChronoField.MONTH_OF_YEAR).toInt(),
        hijrahDate.getLong(ChronoField.DAY_OF_MONTH).toInt()
    )
}

fun readFile(path: String): Sequence<String> {
    return HijriDate::class.java.getResourceAsStream(path)
        ?.bufferedReader()?.run {
            sequence {
                useLines {
                    it.forEach { line -> yield(line) }
                }
            }
        } ?: throw FileNotFoundException(path)
}