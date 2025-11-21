package dev.metinkale.openprayertimes.sources.features

import dev.metinkale.openprayertimes.Entry
import dev.metinkale.openprayertimes.geo.Geolocation
import dev.metinkale.openprayertimes.sources.Source

interface SearchableSource : Source {
    suspend fun search(query: String, location: Geolocation? = null): Entry?
}

