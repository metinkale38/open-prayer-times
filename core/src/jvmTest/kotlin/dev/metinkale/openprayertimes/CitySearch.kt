package dev.metinkale.openprayertimes

import dev.metinkale.openprayertimes.sources.CalcImpl
import dev.metinkale.openprayertimes.sources.LondonImpl
import dev.metinkale.openprayertimes.sources.Source
import dev.metinkale.openprayertimes.sources.features.SearchableSource
import dev.metinkale.openprayertimes.utils.normalize
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CitySearch {
    init {
        Configuration.languages = listOf("de","tr","en")
    }

    @Test
    fun checkSearch() = runBlocking {
        sampleCities.forEach { sample ->
            SearchEntry.search(sample.name).let { result ->
                val searchable = Source.entries.mapNotNull { it as? SearchableSource } - sample.exclude - Source.London + Source.Calc

                searchable.forEach { source ->
                    val match = result.find { it.source == source }
                    assertNotNull(match, sample.name + " not found in " + source.name)
                    assertEquals(
                        match.localizedName.normalize(),
                        sample.name.normalize(),
                        sample.name + " wrong name in " + source.name
                    )
                    assertEquals(
                        match.country,
                        sample.country,
                        sample.name + " wrong country in " + source.name
                    )
                }


            }
        }
    }


}

