package dev.metinkale.prayertimes.core

import com.google.maps.ElevationApi
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.TimeZoneApi
import com.google.maps.model.AddressComponentType
import com.google.maps.model.AddressType
import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng
import dev.metinkale.prayertimes.core.utils.roundLatLng
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinTimeZone
import kotlinx.serialization.Serializable

object Geocoder {
    private val geoApiContext = GeoApiContext.Builder().apiKey(Secrets.googleApiKey).build()

    fun getTimeZone(lat: Double, lng: Double): TimeZone {
        return TimeZoneApi.getTimeZone(geoApiContext, LatLng(lat, lng)).await().toZoneId().toKotlinTimeZone()
    }

    fun getElevation(lat: Double, lng: Double): Double {
        return ElevationApi.getByPoint(geoApiContext, LatLng(lat, lng)).await().elevation.roundLatLng()
    }

    fun reverse(lat: Double, lng: Double, lang: String): GeoLocation? {
        val results: Array<GeocodingResult> =
            GeocodingApi.reverseGeocode(geoApiContext, LatLng(lat, lng)).resultType(AddressType.LOCALITY).language(lang)
                .await()

        return results.firstOrNull()?.let {
            GeoLocation(
                it.geometry.location.lat.roundLatLng(),
                it.geometry.location.lng.roundLatLng(),
                it.addressComponents.find { it.types.contains(AddressComponentType.COUNTRY) }?.shortName ?: "",
                it.addressComponents.filterNot { it.types.contains(AddressComponentType.COUNTRY) }.map { it.longName },
            )
        }
    }

    fun search(q: String, lang: String): GeoLocation? {
        val results: Array<GeocodingResult> =
            GeocodingApi.geocode(geoApiContext, q).language(lang).resultType(AddressType.LOCALITY).await()

        return results.firstOrNull()?.let {
            GeoLocation(
                it.geometry.location.lat.roundLatLng(),
                it.geometry.location.lng.roundLatLng(),
                it.addressComponents.find { it.types.contains(AddressComponentType.COUNTRY) }?.shortName ?: "",
                it.addressComponents.filterNot { it.types.contains(AddressComponentType.COUNTRY) }.map { it.longName },
            )
        }
    }


    @Serializable
    data class GeoLocation(
        val lat: Double = 0.0,
        val lng: Double = 0.0,
        val country: String,
        val name: List<String>,
    )

    @Serializable
    data class Address(
        val village: String? = null,
        val province: String? = null,
        val region: String? = null,
        val city: String? = null,
        val county: String? = null,
        val state: String? = null,
        val country: String? = null,
        val country_code: String? = null,
    )
}