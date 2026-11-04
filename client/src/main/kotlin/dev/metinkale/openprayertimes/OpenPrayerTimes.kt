package dev.metinkale.openprayertimes

import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.URISyntaxException
import java.net.URLEncoder


object OpenPrayerTimes {
    const val DEFAULT_BASE_URL = "https://opt.mk38.de"

    @Volatile
    private var currentBaseUrl = DEFAULT_BASE_URL

    internal fun createHttpClient(): HttpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    @Volatile
    internal var httpClient: HttpClient = createHttpClient()

    val client: HttpClient
        get() = httpClient

    fun configure(baseUrl: String) {
        currentBaseUrl = normalizeBaseUrl(baseUrl)
    }

    fun baseUrl(): String = currentBaseUrl

    private fun normalizeBaseUrl(value: String): String {
        val trimmed = value.trim()
        require(trimmed.isNotEmpty()) { "Base URL must not be blank" }

        val normalized = trimmed.removeSuffix("/")
        val uri = try {
            URI(normalized)
        } catch (e: URISyntaxException) {
            throw IllegalArgumentException("Invalid base URL: $value", e)
        }

        require(uri.scheme.equals("http", true) || uri.scheme.equals("https", true)) {
            "Base URL must use http or https: $value"
        }
        require(!uri.host.isNullOrBlank()) { "Base URL must contain a host: $value" }
        require(uri.query == null && uri.fragment == null) {
            "Base URL must not contain a query or fragment: $value"
        }

        return normalized
    }

    private fun endpoint(path: String): String = "${currentBaseUrl.trimEnd('/')}/$path"

    suspend fun search(latitude: Double, longitude: Double): List<City> {
        return client.get(endpoint("search?lat=$latitude&lng=$longitude")).body<List<City>>()
    }

    suspend fun list(path: List<String>): Pair<List<String>?, City?> {
        val json = client.get(endpoint(path.joinToString("/"))).bodyAsText()
        return when (json[0]) {
            '[' -> Json.decodeFromString<List<String>>(json) to null
            else -> null to Json.decodeFromString<City>(json)
        }
    }

    suspend fun search(query: String): List<City> {
        return client.get(endpoint("search?q=${URLEncoder.encode(query, "UTF-8")}")).body<List<City>>()
    }

    suspend fun fetch(provider: Provider, key: String): List<DayTimes> {
        return client.get(endpoint("${provider.name}/${key}/json")).body<List<DayTimes>>()
    }
}
