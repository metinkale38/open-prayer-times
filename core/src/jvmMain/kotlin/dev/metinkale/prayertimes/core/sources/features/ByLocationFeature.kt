package dev.metinkale.prayertimes.core.sources.features

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.geo.Geolocation

interface ByLocationFeature {
    suspend fun search(geolocation: Geolocation): Entry?
}