package dev.metinkale.openprayertimes.server.feature

import dev.metinkale.openprayertimes.City
import dev.metinkale.openprayertimes.Provider
import dev.metinkale.openprayertimes.server.db.table.DiyanetTable
import dev.metinkale.openprayertimes.server.db.table.IGMGTable
import dev.metinkale.openprayertimes.server.db.table.NVCTable
import dev.metinkale.openprayertimes.server.db.table.SemerkandTable


interface SearchableProvider {
    suspend fun findNearestCity(
        userLat: Float,
        userLng: Float
    ): City?

    suspend fun search(query: String, nearbyLat: Float? = null, nearbyLng: Float? = null): City?


    companion object {
        private val providers: List<SearchableProvider> = Provider.entries.map {
            when (it) {
                Provider.Diyanet -> DiyanetTable
                Provider.IGMG -> IGMGTable
                Provider.London -> LondonProvider
                Provider.NVC -> NVCTable
                Provider.Calc -> CalcProvider
                Provider.Semerkand -> SemerkandTable
            }
        }

        suspend fun findNearestCity(
            userLat: Float,
            userLng: Float
        ): List<City> {
            return providers.mapNotNull { it.findNearestCity(userLat, userLng) }
        }

        suspend fun search(
            query: String
        ): List<City> {
            val calcResult = CalcProvider.search(query)
            return (providers.filter { it != CalcProvider }
                .mapNotNull {
                    it.search(query, calcResult?.lat, calcResult?.lng)
                        ?: calcResult?.let { r -> it.findNearestCity(r.lat!!, r.lng!!) }
                } + calcResult).filterNotNull()
        }
    }
}
