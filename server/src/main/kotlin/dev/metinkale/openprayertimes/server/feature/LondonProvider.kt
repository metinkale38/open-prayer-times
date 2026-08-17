package dev.metinkale.openprayertimes.server.feature

import dev.metinkale.openprayertimes.City
import dev.metinkale.openprayertimes.Provider
import kotlinx.datetime.TimeZone

object LondonProvider : SearchableProvider, ListableProvider {
    private val city = City(
        id = "0",
        lat = 51.5073219f,
        lng = -0.1276473f,
        elv = 0f,
        country = "GB",
        names = listOf("London"),
        provider = Provider.London,
        timezone = TimeZone.of("Europe/London")
    )

    override suspend fun getChildren(path: List<String>): List<String> {
        return if (path.isEmpty())
            return listOf("London")
        else emptyList()
    }

    override suspend fun getCity(path: List<String>): City = city
    override suspend fun getCityById(id: String): City = city

    override suspend fun findNearestCity(
        userLat: Float,
        userLng: Float
    ): City? {
        if (userLat in city.lat!! - 1..city.lat!! + 1 && userLng in city.lng!! - 1..city.lng!! + 1)
            return city
        else
            return null
    }

    override suspend fun search(query: String, nearbyLat: Float?, nearbyLng: Float?): City? {
        if (query.lowercase().contains("london")) return city
        else return null
    }
}