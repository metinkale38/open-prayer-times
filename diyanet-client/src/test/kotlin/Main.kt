package dev.metinkale.diyanet.client

suspend fun main() {

    val api = DiyanetApiClient()

    // Login
    api.authApi().login(System.getenv("DIYANET_USER"),System.getenv("DIYANET_PASSWORD"))

    // Länder
    val countries = api.placeApi().getCountries()

    val germany = countries.data.first { it.code == "GERMANY" }
    val states = api.placeApi().getStates(germany.id)
    val cities = api.placeApi().getCities(states.data.first().id)

    // Gebetszeiten
    val daily = api.prayerApi().getDaily(cities.data.first().id)

    println(daily)
}