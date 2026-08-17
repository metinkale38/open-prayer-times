package dev.metinkale.openprayertimes.server.provider

import dev.metinkale.diyanet.client.DiyanetApiClient
import dev.metinkale.openprayertimes.DayTimes
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.slf4j.LoggerFactory

object DiyanetProvider : PrayerTimesFetcher {

    val logger = LoggerFactory.getLogger("DiyanetProvider");
    val api = DiyanetApiClient(System.getenv("DIYANET_USER"), System.getenv("DIYANET_PASSWORD"))


    override suspend fun getPrayerTimes(key: String): List<DayTimes> {
        val result = api.prayerTimes.getYearly(key.toInt())
        if (!result.success) {
            logger.error("Could not sync $key: {}", result.message)
            throw RuntimeException("Diyanet API returned an error: ${result.message}")
        } else {
            return result.data!!.map {
                DayTimes(
                    it.gregorianDateShort.split(".").let { LocalDate(it[2].toInt(), it[1].toInt(), it[0].toInt()) },
                    fajr = LocalTime.parse(it.fajr),
                    sun = LocalTime.parse(it.sunrise),
                    dhuhr = LocalTime.parse(it.dhuhr),
                    asr = LocalTime.parse(it.asr),
                    maghrib = LocalTime.parse(it.maghrib),
                    ishaa = LocalTime.parse(it.isha),
                )
            }
        }
    }
}