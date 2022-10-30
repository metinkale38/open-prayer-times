package com.metinkale.prayertimes.core

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object Geocoder {
    val json = Json { ignoreUnknownKeys = true }

    suspend fun reverse(lat: Double, lng: Double, lang: String): GeoLocation {
        val response: String = client.get(
            "http://nominatim.openstreetmap.org/reverse?format=json&email=metinkale38@gmail.com&lat=$lat&lon=$lng&accept-language=$lang&zoom=13"
        ).bodyAsText()
        return json.decodeFromString(response)
    }

    suspend fun search(q: String, lang: String): GeoLocation? {
        val response: String = client.get(
            "https://nominatim.openstreetmap.org/search?format=jsonv2&email=metinkale38@gmail.com&q=${
                q.replace(" ", "+")
            }&accept-language=$lang&zoom=13&limit=1&addressdetails=1"
        ).bodyAsText()
        return json.decodeFromString(ListSerializer(GeoLocation.serializer()), response).firstOrNull()
    }


    @Serializable
    data class GeoLocation(
        val lat: Double = 0.0,
        val lon: Double = 0.0,
        val name: String? = null,
        val address: Address = Address(),
        val type: String? = null
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