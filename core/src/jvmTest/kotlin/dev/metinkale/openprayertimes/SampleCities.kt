package dev.metinkale.openprayertimes

import dev.metinkale.openprayertimes.sources.Source

data class TestEntry(
    val name: String,
    val country: String,
    val lat: Double,
    val lng: Double,
    val exclude: Set<Source> = emptySet()
)


val sampleCities = listOf(
    TestEntry("Braunschweig", "DE", 52.266666, 10.516667),
    TestEntry("Hannover", "DE", 52.37227, 9.73815),
    TestEntry("Berlin", "DE", 52.520008, 13.404954),
    TestEntry("Kayseri", "TR", 38.7312, 35.478729),
    TestEntry("Ankara", "TR", 39.92109, 32.85391),
    TestEntry("Istanbul", "TR", 41.01384, 28.94966),
    TestEntry("Develi", "TR", 38.39056, 35.49222, setOf(Source.IGMG)),
    TestEntry("Beypazarı", "TR", 40.1675, 31.921111, setOf(Source.IGMG)),
    TestEntry("Denizli", "TR", 37.7667, 29.0833, setOf(Source.IGMG))
)