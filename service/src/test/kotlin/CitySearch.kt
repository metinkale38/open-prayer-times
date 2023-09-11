import dev.metinkale.prayertimes.core.sources.Diyanet
import dev.metinkale.prayertimes.core.sources.NVC
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CitySearch {

    @ParameterizedTest(name = "search for {0}")
    @ValueSource(
        strings = [
            "Braunschweig",
            "Hannover",
            "Berlin",
            "Kayseri̇",
            "Ankara",
            "İstanbul",
            "Develi̇",
            "Beypazari",
            "Kartal",
            "Deni̇zli̇",
        ]
    )
    fun checkSearchDiyanet(city: String) {
        val result = runBlocking { Diyanet.search(city) }
        assertNotNull(result)
        assertEquals(city, result.names("tr").first())
    }


}

