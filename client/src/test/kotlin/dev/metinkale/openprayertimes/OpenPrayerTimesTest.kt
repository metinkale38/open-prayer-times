package dev.metinkale.openprayertimes

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OpenPrayerTimesTest {

    @BeforeTest
    fun setUp() {
        OpenPrayerTimes.configure(OpenPrayerTimes.DEFAULT_BASE_URL)
        OpenPrayerTimes.httpClient = OpenPrayerTimes.createHttpClient()
    }

    @AfterTest
    fun tearDown() {
        OpenPrayerTimes.httpClient.close()
        OpenPrayerTimes.httpClient = OpenPrayerTimes.createHttpClient()
        OpenPrayerTimes.configure(OpenPrayerTimes.DEFAULT_BASE_URL)
    }

    @Test
    fun testDefaultBaseUrl() {
        assertEquals("https://opt.mk38.de", OpenPrayerTimes.DEFAULT_BASE_URL)
        assertEquals(OpenPrayerTimes.DEFAULT_BASE_URL, OpenPrayerTimes.baseUrl())
    }

    @Test
    fun testCustomBaseUrl() {
        OpenPrayerTimes.configure("https://prayer.example.org")

        assertEquals("https://prayer.example.org", OpenPrayerTimes.baseUrl())
    }

    @Test
    fun testTrimsWhitespaceAndTrailingSlash() {
        OpenPrayerTimes.configure("  https://example.org/prayer/  ")

        assertEquals("https://example.org/prayer", OpenPrayerTimes.baseUrl())
    }

    @Test
    fun testAcceptsHttp() {
        OpenPrayerTimes.configure("http://localhost:8080/prayer")

        assertEquals("http://localhost:8080/prayer", OpenPrayerTimes.baseUrl())
    }

    @Test
    fun testRejectsInvalidBaseUrls() {
        val current = OpenPrayerTimes.baseUrl()

        listOf(
            "",
            "   ",
            "example.org",
            "ftp://example.org",
            "https://",
            "https://example.org?query=1"
        ).forEach { value ->
            assertFailsWith<IllegalArgumentException> { OpenPrayerTimes.configure(value) }
            assertEquals(current, OpenPrayerTimes.baseUrl())
        }
    }

    @Test
    fun testRequestsUseCustomBaseUrl() = runBlocking {
        val requests = mutableListOf<String>()
        val engine = MockEngine { request ->
            requests += request.url.toString()
            respond(
                content = "[]",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        OpenPrayerTimes.httpClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json()
            }
        }
        OpenPrayerTimes.configure("https://example.org/prayer/")

        OpenPrayerTimes.search("Berlin")
        OpenPrayerTimes.search(52.5, 13.4)
        OpenPrayerTimes.list(listOf("Diyanet", "Berlin"))
        OpenPrayerTimes.fetch(Provider.Diyanet, "city-key")

        assertEquals(
            listOf(
                "https://example.org/prayer/search?q=Berlin",
                "https://example.org/prayer/search?lat=52.5&lng=13.4",
                "https://example.org/prayer/Diyanet/Berlin",
                "https://example.org/prayer/Diyanet/city-key/json"
            ),
            requests
        )
    }
}
