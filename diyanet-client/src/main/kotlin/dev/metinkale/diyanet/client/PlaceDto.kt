package dev.metinkale.diyanet.client

import kotlinx.serialization.Serializable

@Serializable
data class ListData<T>(
    val data: List<T>
)

@Serializable
data class Country(
    val id: Int,
    val code: String,
    val name: String
)

@Serializable
data class State(
    val id: Int,
    val name: String
)

@Serializable
data class City(
    val id: Int,
    val name: String
)

@Serializable
data class District(
    val id: Int,
    val name: String
)