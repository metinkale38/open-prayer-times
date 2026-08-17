package dev.metinkale.openprayertimes

import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable


@Serializable
data class City(
    val id: String,
    val lat: Float?,
    val lng: Float?,
    val elv: Float? = null,
    val country: String,
    val names: List<String>,
    val provider: Provider,
    val timezone: TimeZone? = null
)

