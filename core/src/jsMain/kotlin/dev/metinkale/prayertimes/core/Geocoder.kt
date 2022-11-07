package dev.metinkale.prayertimes.core

import dev.metinkale.prayertimes.core.utils.roundLatLng
import kotlinx.datetime.TimeZone
import kotlinx.datetime.internal.JSJoda.Instant


actual object Geocoder {
    init {
        TimezoneInit.init()
    }


    actual suspend fun getTimeZone(lat: Double, lng: Double): TimeZone {
        val response =
            HttpClient.get(
                "https://maps.googleapis.com/maps/api/timezone/json?location=$lat%2C$lng&timestamp=${
                    Instant.now().epochSecond()
                }&key=${Secrets.googleApiKey}"
            )

        return TimeZone.of(JSON.parse<dynamic>(response).timeZoneId as String)
    }

    actual suspend fun getElevation(lat: Double, lng: Double): Double {
        val response =
            HttpClient.get("https://maps.googleapis.com/maps/api/elevation/json?locations=$lat%2C$lng&key=${Secrets.googleApiKey}")

        return JSON.parse<dynamic>(response).results[0].elevation as Double
    }

    actual suspend fun reverse(
        lat: Double,
        lng: Double,
        lang: String
    ): GeocoderResult? {
        val response =
            HttpClient.get("https://maps.googleapis.com/maps/api/geocode/json?latlng=$lat%2C$lng&result_type=locality&language=$lang&key=${Secrets.googleApiKey}")

        val json = JSON.parse<dynamic>(response)
        val result = json.results[0]
        val addressComponents: Array<dynamic> = result.address_components as Array<dynamic>
        return GeocoderResult(
            (result.geometry.location.lat as Double).roundLatLng(),
            (result.geometry.location.lng as Double).roundLatLng(),
            (addressComponents.find { "country" in (it.types as Array<String>) }?.short_name ?: "") as String,
            addressComponents.filterNot { "country" in (it.types as Array<String>) }.map { it.long_name as String },
        )
    }

    actual suspend fun search(q: String, lang: String): GeocoderResult? {
        val response =
            HttpClient.get("https://maps.googleapis.com/maps/api/geocode/json?address=$q&result_type=locality&language=$lang&key=${Secrets.googleApiKey}")

        val json = JSON.parse<dynamic>(response)
        val result = json.results[0]
        val addressComponents: Array<dynamic> = result.address_components as Array<dynamic>
        return GeocoderResult(
            (result.geometry.location.lat as Double).roundLatLng(),
            (result.geometry.location.lng as Double).roundLatLng(),
            (addressComponents.find { "country" in (it.types as Array<String>) }?.short_name ?: "") as String,
            addressComponents.filterNot { "country" in (it.types as Array<String>) }.map { it.long_name as String },
        )
    }

}
