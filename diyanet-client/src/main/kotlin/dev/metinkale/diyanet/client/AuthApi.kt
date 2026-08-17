package dev.metinkale.diyanet.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApi(private val client: HttpClient) {

    suspend fun login(email: String, password: String): Response<TokenData> =
        client.post("/Auth/Login") {
            setBody(LoginRequest(email, password))
        }.body()

    suspend fun refreshToken(refreshToken: String): Response<TokenData> =
        client.get("/Auth/RefreshToken/$refreshToken").body()
}