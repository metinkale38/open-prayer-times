package dev.metinkale.diyanet.client

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.util.Date
import kotlin.math.log

class DiyanetApiClient(private val username: String, private val password: String) {

    val logger = LoggerFactory.getLogger("DiyanetApiClient")

    val BASE_URL = "https://awqatsalah.diyanet.gov.tr"
    private var accessToken: String? = null
    private var refreshToken: String? = null

    private val baseClient = HttpClient(CIO) {

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }

        defaultRequest {
            url(BASE_URL)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    private val client = baseClient.config {
        install(createClientPlugin("TokenInterceptor") {
            onRequest { request, _ ->
                request.headers.append("Authorization", "Bearer ${getAccessToken()}")
            }
        })


    }

    private suspend fun getAccessToken(): String {
        return accessToken?.takeUnless { isTokenExpired(it) }
            ?: refreshToken?.takeUnless { isTokenExpired(it) }?.let {
                logger.info("RefreshToken")
                auth.refreshToken(refreshToken!!).save()
            } ?: run {
                logger.info("Login")
                auth.login(username, password).save()
            }
    }

    private fun Response<TokenData>.save(): String {
        if (!success) {
            logger.error("Login/Refresh failed: {}", message)
            throw RuntimeException("Failed to login/refresh diyanet api: $message")
        } else {
            accessToken = data!!.accessToken
            refreshToken = data.refreshToken
            return data.accessToken

        }
    }


    private fun isTokenExpired(token: String): Boolean {
        return try {
            val decodedJWT = JWT.decode(token)
            val expiresAt = decodedJWT.expiresAt
            expiresAt == null || !expiresAt.after(Date())
        } catch (e: JWTDecodeException) {
            true
        }
    }

    private val auth = AuthApi(baseClient)
    val places = PlaceApi(client)
    val prayerTimes = PrayerTimeApi(client)
}