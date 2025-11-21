package dev.metinkale.diyanet.client

import kotlinx.serialization.Serializable
@Serializable
data class AuthResponse(
    val data: TokenData,
    val success: Boolean,
    val message: String? = null
)

@Serializable
data class TokenData(
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String
)