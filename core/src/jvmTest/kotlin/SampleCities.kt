import dev.metinkale.prayertimes.core.sources.Source

data class Entry(
    val name: String,
    val country: String,
    val lat: Double,
    val lng: Double,
    val exclude: Set<Source> = emptySet()
)


val sampleCities = listOf(
    Entry("Braunschweig", "DE", 52.266666, 10.516667),
    Entry("Hannover", "DE", 52.37227, 9.73815),
    Entry("Berlin", "DE", 52.520008, 13.404954),
    Entry("Kayseri", "TR", 38.7312, 35.478729),
    Entry("Ankara", "TR", 39.92109, 32.85391),
    Entry("Istanbul", "TR", 41.01384, 28.94966),
    Entry("Develi", "TR", 38.39056, 35.49222, setOf(Source.IGMG)),
    Entry("Beypazarı", "TR", 40.1675, 31.921111, setOf(Source.IGMG)),
    Entry("Denizli", "TR", 37.7667, 29.0833, setOf(Source.IGMG))
)