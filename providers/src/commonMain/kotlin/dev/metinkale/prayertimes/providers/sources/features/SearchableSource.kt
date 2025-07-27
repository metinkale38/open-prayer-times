package dev.metinkale.prayertimes.providers.sources.features

import dev.metinkale.prayertimes.providers.Entry
import dev.metinkale.prayertimes.providers.geo.Geolocation
import dev.metinkale.prayertimes.providers.sources.Source

interface SearchableSource : Source {
    suspend fun search(query: String, location: Geolocation? = null): Entry?
}

