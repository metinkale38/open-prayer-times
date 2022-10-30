package com.metinkale.prayertimes.core.sources.features

import com.metinkale.prayertimes.core.Entry
import com.metinkale.prayertimes.core.Geocoder

interface ByLocationFeature {
    fun search(geolocation: Geocoder.GeoLocation): Entry?
}