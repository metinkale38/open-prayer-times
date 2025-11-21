package dev.metinkale.openprayertimes.sources.features

import dev.metinkale.openprayertimes.Entry
import dev.metinkale.openprayertimes.geo.Geolocation
import dev.metinkale.openprayertimes.sources.Source

interface LocatableSource : Source {
    suspend fun search(geolocation: Geolocation): Entry?
}