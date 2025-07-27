package dev.metinkale.prayertimes.db

import dev.metinkale.prayertimes.providers.DayTimes
import dev.metinkale.prayertimes.providers.sources.Source
import dev.metinkale.prayertimes.Platform
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.number
import org.metinkale.prayertimes.db.TimesDatabase

object DatabaseHelper {
    val driver = Platform.createSqlDelightDriver(TimesDatabase.Schema, "jdbc:sqlite:times.db")
    val database = TimesDatabase(driver)

    init {
        TimesDatabase.Schema.create(driver)
    }

    fun persist(source: Source, key: String, vararg dt: DayTimes) {
        database.transaction {
            dt.forEach {
                database.timesQueries.insert(
                    source.name,
                    key,
                    it.date.toLong(),
                    it.fajr.toString(),
                    it.sun.toString(),
                    it.dhuhr.toString(),
                    it.asr.toString(),
                    it.maghrib.toString(),
                    it.ishaa.toString(),
                    it.asrHanafi?.toString(),
                    it.sabah?.toString()
                )
            }
        }
    }

    fun get(source: Source, key: String, from: LocalDate, to: LocalDate = from): List<DayTimes> {
        return database.timesQueries.selectAll(source.name, key, from.toLong(), to.toLong()).executeAsList().map {
            DayTimes(
                it.date.toLocalDate(),
                LocalTime.parse(it.fajr),
                LocalTime.parse(it.sun),
                LocalTime.parse(it.dhuhr),
                LocalTime.parse(it.asr),
                LocalTime.parse(it.maghrib),
                LocalTime.parse(it.ishaa),
                it.asrHanafi?.let(LocalTime.Companion::parse),
                it.sabah?.let(LocalTime.Companion::parse)
            )
        }
    }

    private fun LocalDate.toLong() = year * 10000L + month.number * 100L + day
    private fun Long.toLocalDate(): LocalDate {
        val year = (this / 10000).toInt()
        val month = ((this % 10000) / 100).toInt()
        val day = (this % 100).toInt()
        return LocalDate(year, month, day)
    }

}