import dev.metinkale.prayertimes.core.geo.Geocoder
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeocoderTest {

    data class Entry(val name: String, val lat: Double, val lng: Double)

    @Test
    fun searchByLocation() = runBlocking {
        list.forEach {
            assertEquals(it.name, Geocoder.byLocation(it.lat, it.lng)?.name?:"")
        }
    }

    @Test
    fun searchByName() = runBlocking {
        list.forEach { entry ->
            Geocoder.searchByName(entry.name)?.let {
                assertTrue(abs(entry.lat - it.lat) < 1.0, entry.name + " Lat")
                assertTrue(abs(entry.lng - it.lng) < 1.0, entry.name + " Lng")
            }
        }
    }

    companion object {
        val list = listOf(
            Entry("Braunschweig", 52.266666, 10.516667),
            Entry("Hannover", 52.37227, 9.73815),
            Entry("Berlin", 52.520008, 13.404954),
            Entry("Kayseri", 38.7312, 35.478729),
            Entry("Ankara", 39.92109, 32.85391),
            Entry("Istanbul", 41.01384, 28.94966),
            Entry("Develi", 38.39056, 35.49222),
            Entry("Beypazarı", 40.1675, 31.921111),
            Entry("Denizli", 37.7667, 29.0833)
        )


    }
}