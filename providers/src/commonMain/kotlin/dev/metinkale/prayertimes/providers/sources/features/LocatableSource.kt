package dev.metinkale.prayertimes.providers.sources.features

import dev.metinkale.prayertimes.providers.Entry
import dev.metinkale.prayertimes.providers.geo.Geolocation
import dev.metinkale.prayertimes.providers.sources.Source

interface LocatableSource : Source {
    suspend fun search(geolocation: Geolocation): Entry?
}