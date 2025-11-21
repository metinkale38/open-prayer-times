package dev.metinkale.openprayertimes

import dev.metinkale.openprayertimes.geo.Geocoder
import dev.metinkale.openprayertimes.router.parallelMap
import dev.metinkale.openprayertimes.sources.Source
import dev.metinkale.openprayertimes.sources.features.LocatableSource
import dev.metinkale.openprayertimes.sources.features.IterableSource
import dev.metinkale.openprayertimes.sources.features.SearchableSource

object SearchEntry {
    suspend fun search(query: String): List<Entry> {
        val geo = Geocoder.searchByName(query)

        return Source.entries.parallelMap { source ->
            (source as? SearchableSource)?.search(query, geo)
                ?: geo?.let { source as? LocatableSource }?.search(geo)
        }.filterNotNull()
    }

    suspend fun search(lat: Double, lng: Double): List<Entry> {
        val geo = Geocoder.byLocation(lat, lng)

        return geo?.let { Source.entries }?.mapNotNull { it as? LocatableSource }
            ?.parallelMap { it.search(geo) }?.filterNotNull() ?: emptyList()
    }

    suspend fun list(path: List<String>): Pair<List<String>?, Entry?> {
        return if (path.isEmpty()) {
            Source.entries.mapNotNull { it as? IterableSource }.map { it.name }
                .let { it to null }
        } else {
            val source = Source.valueOf(path[0]) as IterableSource
            source.list(path.drop(1))
        }
    }
}
