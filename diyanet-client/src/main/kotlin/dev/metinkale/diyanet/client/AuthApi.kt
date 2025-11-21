package dev.metinkale.diyanet.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApi(
    private val client: HttpClient,
    private val parent: DiyanetApiClient
) {

    suspend fun login(email: String, password: String): AuthResponse {
        val response: AuthResponse = client.post("/Auth/Login") {
            setBody(LoginRequest(email, password))
        }.body()

        parent.saveTokens(
            response.data.accessToken,
            response.data.refreshToken
        )

        return response
    }

    suspend fun register(request: RegisterRequest): AuthResponse =
        client.post("/Auth/Register") {
            setBody(request)
        }.body()

    suspend fun refreshToken(refreshToken: String): AuthResponse =
        client.get("/Auth/RefreshToken/$refreshToken").body()

    suspend fun forgotPassword(email: String) =
        client.post("/Auth/ForgotPassword") {
            setBody(mapOf("email" to email))
        }
}