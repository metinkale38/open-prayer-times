package dev.metinkale.prayertimes.core.sources.features

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.Geolocation
import dev.metinkale.prayertimes.core.sources.Source

interface ByLocationFeature : Source {
    suspend fun search(geolocation: Geolocation): List<Entry>
}