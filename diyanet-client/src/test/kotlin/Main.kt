package dev.metinkale.diyanet.client

suspend fun main() {

    val api = DiyanetApiClient(System.getenv("DIYANET_USER"), System.getenv("DIYANET_PASSWORD"))

    // Länder
    val countries = api.places.getCountries()

    val germany = countries.data!!.first { it.code == "GERMANY" }
    val states = api.places.getStates(germany.id)
    val cities = api.places.getCities(states.data!!.first().id)

    // Gebetszeiten
    val daily = api.prayerTimes.getYearly(cities.data!!.first().id)

    println(daily)
}