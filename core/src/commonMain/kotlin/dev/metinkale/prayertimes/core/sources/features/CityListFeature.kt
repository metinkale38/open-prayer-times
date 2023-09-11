package dev.metinkale.prayertimes.core.sources.features

import dev.metinkale.prayertimes.core.Configuration
import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.geo.Geolocation
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.Pair


// The default factory is acting as client. The Factory is overwritten in service module for concrete implementation
var cityListFeatureFactory: (source: String) -> CityListFeature = { source ->
    object : CityListFeature {
        override val name: String = source
        val apiUrl: String by lazy { Configuration.API_URL }
        val client by lazy { HttpClient() }

        override suspend fun search(query: String, lang: List<String>): Entry? =
            client.get(apiUrl + "search") {
                parameter("q", query)
                headers.append(HttpHeaders.AcceptLanguage, lang.joinToString(","))
            }.bodyAsText().let { Json.decodeFromString<Entry?>(it) }


        override suspend fun search(geolocation: Geolocation, lang: List<String>): Entry? =
            client.get(apiUrl + "search") {
                parameter("lat", geolocation.lat)
                parameter("lng", geolocation.lng)
                headers.append(HttpHeaders.AcceptLanguage, lang.joinToString(","))
            }.bodyAsText().let { Json.decodeFromString<Entry?>(it) }


        override suspend fun list(path: List<String>, lang: List<String>): Pair<List<String>?, Entry?> =
            client.get(apiUrl + "list") {
                url.appendPathSegments(path)
                headers.append(HttpHeaders.AcceptLanguage, lang.joinToString(","))
            }.bodyAsText().let { Json.decodeFromString(it) }

    }
}

internal fun cityListDelegate(source: String): CityListFeature = object : CityListFeature {
    override val name: String = source
    val wrapper by lazy { cityListFeatureFactory(source) }

    override suspend fun search(query: String, lang: List<String>): Entry? = wrapper.search(query, lang)

    override suspend fun search(geolocation: Geolocation, lang: List<String>): Entry? =
        wrapper.search(geolocation, lang)

    override suspend fun list(path: List<String>, lang: List<String>): Pair<List<String>?, Entry?> =
        wrapper.list(path, lang)


}

interface CityListFeature : ByLocationFeature, SearchFeature {

    val name: String
    override suspend fun search(query: String, lang: List<String>): Entry?
    override suspend fun search(geolocation: Geolocation, lang: List<String>): Entry?
    suspend fun list(path: List<String>, lang: List<String>): Pair<List<String>?, Entry?>
}