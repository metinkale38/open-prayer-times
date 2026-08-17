package dev.metinkale.diyanet.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PlaceApi(private val client: HttpClient) {

    suspend fun getCountries(): Response<List<Country>> =
        client.get("/api/Place/Countries").body()

    suspend fun getStates(countryId: Int): Response<List<State>> =
        client.get("/api/Place/States/$countryId").body()

    suspend fun getCities(stateId: Int): Response<List<City>> =
        client.get("/api/Place/Cities/$stateId").body()

    suspend fun getDistricts(cityId: Int): Response<List<District>> =
        client.get("/api/Place/Districts/$cityId").body()
}