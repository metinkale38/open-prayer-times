package dev.metinkale.hijri

import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.io.FileNotFoundException
import java.time.LocalDate
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.chrono.HijrahEra
import java.time.temporal.ChronoField
import kotlinx.datetime.LocalDate as KLocalDate


internal actual val Platform: IPlatform = object : IPlatform {
    override fun convertFromHijri(date: HijriDate): KLocalDate {
        return LocalDate.from(HijrahChronology.INSTANCE.date(HijrahEra.AH, date.year, date.monthValue, date.day))
            .toKotlinLocalDate()
    }

    override fun convertToHijri(date: KLocalDate): HijriDate {
        val hijrahDate: HijrahDate = HijrahChronology.INSTANCE.date(date.toJavaLocalDate())
        return HijriDate.of(
            hijrahDate.getLong(ChronoField.YEAR_OF_ERA).toInt(),
            hijrahDate.getLong(ChronoField.MONTH_OF_YEAR).toInt(),
            hijrahDate.getLong(ChronoField.DAY_OF_MONTH).toInt()
        )
    }

    override fun readFile(path: String): Sequence<String> {
        return Platform::class.java.getResourceAsStream(path)
            ?.bufferedReader()?.run {
                sequence {
                    useLines {
                        it.forEach { line -> yield(line) }
                    }
                }
            } ?: throw FileNotFoundException(path)
    }

}