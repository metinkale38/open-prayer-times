package com.metinkale.prayertimes.core.sources.features

import com.metinkale.prayertimes.core.Entry

interface CityListFeature {
    fun getCities(): Sequence<Entry>
}