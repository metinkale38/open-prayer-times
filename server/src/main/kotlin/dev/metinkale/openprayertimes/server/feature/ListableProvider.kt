package dev.metinkale.openprayertimes.server.feature

import dev.metinkale.openprayertimes.City
import dev.metinkale.openprayertimes.Provider
import dev.metinkale.openprayertimes.server.db.table.DiyanetTable
import dev.metinkale.openprayertimes.server.db.table.IGMGTable
import dev.metinkale.openprayertimes.server.db.table.NVCTable
import dev.metinkale.openprayertimes.server.db.table.SemerkandTable
import dev.metinkale.openprayertimes.server.db.table.VaktijaTable


interface ListableProvider {

    suspend fun getChildren(path: List<String>): List<String>
    suspend fun getCity(path: List<String>): City?
    suspend fun getCityById(id: String): City?


    companion object {
        private val providers: Map<String, ListableProvider> = Provider.entries.mapNotNull { provider ->
            when (provider) {
                Provider.Diyanet -> DiyanetTable
                Provider.IGMG -> IGMGTable
                Provider.London -> LondonProvider
                Provider.NVC -> NVCTable
                Provider.Calc -> null
                Provider.Semerkand -> SemerkandTable
                Provider.Vaktija -> VaktijaTable
            }?.let { provider.name to it }
        }.toMap()

        suspend fun getChildren(path: List<String>): List<String> {
            if (path.isEmpty()) return providers.keys.map { it }
            return providers[path[0]]?.getChildren(path.drop(1)) ?: return emptyList()
        }

        suspend fun getCity(path: List<String>): City? {
            if (path.size < 2) return null;
            if (path.size == 2) providers[path[0]]?.getCityById(path[1])?.let { return it }
            return providers[path[0]]?.getCity(path.drop(1))
        }

    }

}