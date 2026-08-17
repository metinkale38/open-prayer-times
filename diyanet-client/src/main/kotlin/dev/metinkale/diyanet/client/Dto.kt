package dev.metinkale.diyanet.client

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(val data: T? = null, val success: Boolean, val message: String?)

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
data class Country(
    val id: Int,
    val code: String,
    val name: String
)

@Serializable
data class State(
    val id: Int,
    val name: String
)

@Serializable
data class City(
    val id: Int,
    val name: String
)

@Serializable
data class District(
    val id: Int,
    val name: String
)

@Serializable
data class PrayerTime(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val gregorianDateShort: String,
    val hijriDateShort: String
)

@Serializable
data class DateRangeFilter(
    val CityId: Int,
    val StartDate: String,
    val EndDate: String
)
