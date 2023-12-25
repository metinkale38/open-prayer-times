package dev.metinkale.prayertimes.core.sources.features

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.geo.Geolocation
import dev.metinkale.prayertimes.core.sources.Source

interface SearchFeature : Source {
    suspend fun search(query: String, location: Geolocation? = null): Entry?
}

