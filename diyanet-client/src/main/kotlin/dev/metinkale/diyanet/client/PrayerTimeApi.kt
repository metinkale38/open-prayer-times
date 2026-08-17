package dev.metinkale.diyanet.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.time.LocalDate

class PrayerTimeApi(
    private val client: HttpClient
) {

    suspend fun getDaily(cityId: Int): Response<List<PrayerTime>> =
        client.get("/api/PrayerTime/Daily/$cityId").body()

    suspend fun getMonthly(cityId: Int): Response<List<PrayerTime>> =
        client.get("/api/PrayerTime/Monthly/$cityId").body()


    suspend fun getYearly(cityId: Int): Response<List<PrayerTime>> {
        val now = LocalDate.now()
        val start = now.withDayOfMonth(1)
        val end = start.plusYears(1).minusDays(1)

        val startDate = "${start}T00:00:00"
        val endDate = "${end}T23:59:59"

        return client.post("/api/PrayerTime/DateRange") {
            setBody(DateRangeFilter(CityId = cityId, StartDate = startDate, EndDate = endDate))
        }.body()
    }
}