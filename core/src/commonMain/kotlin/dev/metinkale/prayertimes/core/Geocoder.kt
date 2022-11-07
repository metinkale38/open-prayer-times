package dev.metinkale.prayertimes.core

import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable


@Serializable
data class GeocoderResult(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val country: String,
    val name: List<String>,
)

expect object Geocoder {
    suspend fun getTimeZone(lat: Double, lng: Double): TimeZone
    suspend fun getElevation(lat: Double, lng: Double): Double
    suspend fun reverse(lat: Double, lng: Double, lang: String): GeocoderResult?
    suspend fun search(q: String, lang: String): GeocoderResult?
}