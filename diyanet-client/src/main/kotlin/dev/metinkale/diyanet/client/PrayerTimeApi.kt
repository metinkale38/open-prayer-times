package dev.metinkale.diyanet.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class PrayerTimeApi(
    private val client: HttpClient
) {

    suspend fun getDaily(cityId: Int): PrayerTime =
        client.get("/api/PrayerTime/Daily/$cityId").body()

    suspend fun getMonthly(cityId: Int): List<PrayerTime> =
        client.get("/api/PrayerTime/Monthly/$cityId").body()

    suspend fun getYearly(cityId: Int): List<PrayerTime> =
        client.get("/api/PrayerTime/Yearly/$cityId").body()

    suspend fun getByDateRange(
        cityId: Int,
        startDate: String,
        endDate: String
    ): List<PrayerTime> =
        client.get("/api/PrayerTime/ByDateRange") {
            parameter("cityId", cityId)
            parameter("startDate", startDate)
            parameter("endDate", endDate)
        }.body()
}