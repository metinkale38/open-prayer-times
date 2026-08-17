package dev.metinkale.openprayertimes

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.net.URLEncoder


object OpenPrayerTimes {
    private val HOST = "https://opt.mk38.de"
    val client = io.ktor.client.HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun search(latitude: Double, longitude: Double): List<City> {
        return client.get("$HOST/search?lat=$latitude&lng=$longitude").body<List<City>>()
    }

    suspend fun list(path: List<String>): Pair<List<String>?, City?> {
        val json = client.get("$HOST/${path.joinToString("/")}").bodyAsText()
        return when (json[0]) {
            '[' -> Json.decodeFromString<List<String>>(json) to null
            else -> null to Json.decodeFromString<City>(json)
        }
    }

    suspend fun search(query: String): List<City> {
        return client.get("$HOST/search?q=${URLEncoder.encode(query, "UTF-8")}").body<List<City>>()
    }

    suspend fun fetch(provider: Provider, key: String): List<DayTimes> {
        return client.get("$HOST/${provider.name}/${key}/json").body<List<DayTimes>>()
    }


}