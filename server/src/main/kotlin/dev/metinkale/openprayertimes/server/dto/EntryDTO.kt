package dev.metinkale.openprayertimes.server.dto

import dev.metinkale.openprayertimes.Entry
import dev.metinkale.openprayertimes.geo.Geocoder
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable

@Serializable
data class EntryDTO(
    val lat: Double?,
    val lng: Double?,
    val timezone: TimeZone?
) {
    companion object {
        suspend fun from(entry: Entry) = EntryDTO(
            entry.lat,
            entry.lng,
            entry.timeZone ?: Geocoder.byLocation(entry.lat ?: 0.0, entry.lng ?: 0.0)?.timezone
        )
    }
}