import dev.metinkale.prayertimes.providers.Configuration
import dev.metinkale.prayertimes.providers.SearchEntry
import dev.metinkale.prayertimes.providers.sources.Calc
import dev.metinkale.prayertimes.providers.sources.London
import dev.metinkale.prayertimes.providers.sources.Source
import dev.metinkale.prayertimes.providers.sources.features.SearchFeature
import dev.metinkale.prayertimes.providers.utils.normalize
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CitySearch {
    init {
        Configuration.languages = listOf("de","tr","en")
        // RESOURCES_PATH=C:\Users\metin\Projects\open-prayer-times\core\src\jvmMain\resources\
    }

    @Test
    fun checkSearch() = runBlocking {
        sampleCities.forEach { sample ->
            SearchEntry.search(sample.name).let { result ->
                val searchable = Source.values().mapNotNull { it as? SearchFeature } - sample.exclude - London + Calc


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

