package dev.metinkale.prayertimes.providers

import dev.metinkale.prayertimes.providers.geo.Geocoder
import dev.metinkale.prayertimes.providers.router.parallelMap
import dev.metinkale.prayertimes.providers.sources.Source
import dev.metinkale.prayertimes.providers.sources.features.ByLocationFeature
import dev.metinkale.prayertimes.providers.sources.features.CityListFeature
import dev.metinkale.prayertimes.providers.sources.features.SearchFeature

object SearchEntry {
    suspend fun search(query: String): List<Entry> {
        val geo = Geocoder.searchByName(query)

        return Source.values().parallelMap { source ->
            (source as? SearchFeature)?.search(query, geo)
                ?: geo?.let { source as? ByLocationFeature }?.search(geo)
        }.filterNotNull()
    }

    suspend fun search(lat: Double, lng: Double): List<Entry> {
        val geo = Geocoder.byLocation(lat, lng)

        return geo?.let { Source.values() }?.mapNotNull { it as? ByLocationFeature }
            ?.parallelMap { it.search(geo) }?.filterNotNull() ?: emptyList()
    }

    suspend fun list(path: List<String>): Pair<List<String>?, Entry?> {
        return if (path.isEmpty()) {
            Source.values().mapNotNull { it as? CityListFeature }.map { it.name }
                .let { it to null }
        } else {
            val source = Source.valueOf(path[0]) as CityListFeature
            source.list(path.drop(1))
        }
    }
}
