import dev.metinkale.prayertimes.core.sources.Diyanet
import dev.metinkale.prayertimes.core.sources.NVC
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

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
        assertEquals(1, result.size)
        assertEquals(city, result.first().names("tr").first())
    }


}

