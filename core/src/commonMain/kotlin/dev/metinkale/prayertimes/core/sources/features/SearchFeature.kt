package dev.metinkale.prayertimes.core.sources.features

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.sources.Source

interface SearchFeature  {
    suspend fun search(query: String, lang: List<String>): Entry?
}

