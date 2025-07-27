package dev.metinkale.prayertimes.providers.sources.features

import dev.metinkale.prayertimes.providers.Configuration
import dev.metinkale.prayertimes.providers.Entry
import dev.metinkale.prayertimes.providers.geo.Geolocation
import dev.metinkale.prayertimes.providers.sources.Source
import dev.metinkale.prayertimes.providers.utils.TextSearchEngine
import dev.metinkale.prayertimes.providers.Platform
import kotlin.math.abs


interface IterableSource : Source, LocatableSource, SearchableSource {

    private fun parseCities() = Platform.readFile("/tsv/${name}.tsv").map { line -> Entry.decodeFromString(this, line) }

    val cities: Sequence<Entry>
        get() = parseCities()


    override suspend fun search(query: String, location: Geolocation?): Entry? =
        TextSearchEngine.search(cities, { it.normalizedNames }, { entry ->
            location?.let { loc ->
                entry.lat?.let { lat ->
                    entry.lng?.let { lng ->
                        (abs(loc.lat - lat) + abs(loc.lng - lng) * 1000).toInt()
                    }
                }
            } ?: 0
        }, query)

    override suspend fun search(geolocation: Geolocation): Entry? {
        val lat = geolocation.lat
        val lng = geolocation.lng
        var bestMatch: Entry? = null

        for (entry in cities) {
            if (entry.lat != null && entry.lng != null) {
                val latDist = abs(lat - entry.lat)
                val lngDist = abs(lng - entry.lng)
                if (bestMatch == null) {
                    if (latDist < 2 && lngDist < 2) bestMatch = entry
                } else {
                    if (latDist + lngDist < abs(lat - bestMatch.lat!!) + abs(lng - bestMatch.lng!!)) {
                        bestMatch = entry
                    }
                }
            }
        }

        return bestMatch?.copy(timeZone = geolocation.timezone)
    }

    suspend fun list(
        path: List<String>,
        languages: List<String> = Configuration.languages
    ): Pair<List<String>?, Entry?> =
        if (path.isEmpty()) {
            cities.map { it.country }.distinct().toList().let { it to null }
        } else {
            val country = path[0]
            val parts = path.drop(1)

            val entries: List<Pair<List<String>, Entry>> = cities.filter { it.country == country }
                .map { it.names.reversed().map { it.values } to it }
                .filter { (it, _) ->
                    parts.withIndex().all { (index, name) -> it.getOrNull(index)?.contains(name) == true }
                }.map { (_, entry) -> entry.localizedNames(*languages.toTypedArray()).reversed() to entry }.toList()

            if (entries.size == 1) {
                entries.first().second.let { null to it }
            } else entries.map { it.first.drop(parts.size).first() }.distinct().let { it to null }
        }
}