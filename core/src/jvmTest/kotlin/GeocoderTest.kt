import dev.metinkale.prayertimes.core.geo.Geocoder
import dev.metinkale.prayertimes.core.utils.normalize
import kotlinx.coroutines.runBlocking
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeocoderTest {


    @Test
    fun searchByLocation() = runBlocking {
        sampleCities.forEach {
            assertEquals(it.name.normalize(), Geocoder.byLocation(it.lat, it.lng)?.name?.normalize()?:"")
        }
    }

    @Test
    fun searchByName() = runBlocking {
        sampleCities.forEach { entry ->
            Geocoder.searchByName(entry.name)?.let {
                assertTrue(abs(entry.lat - it.lat) < 2.0, entry.name + " Lat")
                assertTrue(abs(entry.lng - it.lng) < 2.0, entry.name + " Lng")
                assertEquals(entry.country,  entry.country,entry.name + " Country")
            }
        }
    }

}