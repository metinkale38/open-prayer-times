package dev.metinkale.prayertimes.core.geo

import dev.metinkale.prayertimes.core.utils.normalize
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable

@Serializable
data class Geolocation(
    val name: String,
    val alternateNames: String,
    val lat: Double,
    val lng: Double,
    val country: String,
    val elv: Double,
    val zoneId: String,
) {
    internal constructor(line: List<String>) : this(
        line[0],
        line[1],
        line[2].toDouble(),
        line[3].toDouble(),
        line[4],
        line[5].toDouble(),
        line[6]
    )

    val timezone by lazy { TimeZone.of(zoneId) }

    internal val normalizedNames by lazy {
        listOf(
            name.normalize(),
            *alternateNames.split(",").map { it.normalize() }.toTypedArray()
        )
    }
}