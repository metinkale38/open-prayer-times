package dev.metinkale.openprayertimes.server.provider

import dev.metinkale.openprayertimes.DayTimes
import dev.metinkale.openprayertimes.Provider
import io.ktor.client.*

sealed interface PrayerTimesFetcher {
    val httpClient: HttpClient get() = Companion.httpClient

    suspend fun getPrayerTimes(key: String): List<DayTimes>

    companion object {
        private val httpClient: HttpClient = HttpClient()

        public fun find(provider: Provider): PrayerTimesFetcher = when (provider) {
            Provider.Diyanet -> DiyanetProvider
            Provider.IGMG -> IGMGProvider
            Provider.London -> LondonProvider
            Provider.Semerkand -> SemerkandProvider
            Provider.NVC -> NVCProvider
            Provider.Calc -> throw RuntimeException("Calc provider is not supported on the server")
        }
    }

}