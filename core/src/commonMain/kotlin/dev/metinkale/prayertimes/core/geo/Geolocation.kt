package dev.metinkale.prayertimes.core.geo

import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable

@Serializable
data class Geolocation(
    val lat: Double,
    val lng: Double,
    val elv: Double,
    val country: String,
    val name: List<String>,
    val timezone: TimeZone,
)