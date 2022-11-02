package dev.metinkale.prayertimes.core.sources.features

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.Geocoder
import dev.metinkale.prayertimes.core.sources.Source

interface ByLocationFeature : Source {
    suspend fun search(geolocation: Geocoder.GeoLocation): List<Entry>
}