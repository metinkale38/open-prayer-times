package dev.metinkale.prayertimes.core.sources.features

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.geo.Geolocation
import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.utils.SearchEngine
import dev.metinkale.prayertimes.core.utils.readFile
import kotlin.Pair
import kotlin.math.abs


interface CityListFeature : Source, ByLocationFeature, SearchFeature {
    private val cities: Sequence<Entry>
        get() = readFile("/tsv/${name}.tsv").lineSequence()
            .map { line -> Entry.decodeFromString(this, line) }


    override suspend fun search(query: String): Entry? =
        SearchEngine.search(cities, { it.normalizedNames }, query)

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

    suspend fun list(path: List<String>): Pair<List<String>?, Entry?> =
        if (path.isEmpty()) {
            cities.map { it.country }.distinct().toList().let { it to null }
        } else {
            val country = path[0]
            val parts = path.drop(1)


            val entries = cities.filter { it.country == country }
                .map { it.localizedNames.reversed() to it }
                .filter { (it, _) ->
                    parts.withIndex().all { (index, name) -> it.getOrNull(index) == name }
                }.toList()

            if (entries.size == 1) {
                entries.first().second.let { null to it }
            } else entries.map { it.first.drop(parts.size).first() }.distinct().let { it to null }
        }
}