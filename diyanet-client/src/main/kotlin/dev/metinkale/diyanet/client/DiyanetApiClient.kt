package dev.metinkale.diyanet.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class DiyanetApiClient {

    val BASE_URL = "https://awqatsalah.diyanet.gov.tr"
    private var accessToken: String? = null
    private var refreshToken: String? = null

    private val client = HttpClient(CIO) {

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }


        defaultRequest {
            url(BASE_URL)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            accessToken?.let {
                header("Authorization", "Bearer $it")
            }
        }
    }

    fun authApi() = AuthApi(client, this)
    fun placeApi() = PlaceApi(client)
    fun prayerApi() = PrayerTimeApi(client)

    internal fun saveTokens(access: String, refresh: String) {
        accessToken = access
        refreshToken = refresh
    }

    suspend fun refreshIfNeeded() {
        refreshToken?.let {
            val response = authApi().refreshToken(it)
            saveTokens(response.data.accessToken, response.data.refreshToken)
        }
    }
}