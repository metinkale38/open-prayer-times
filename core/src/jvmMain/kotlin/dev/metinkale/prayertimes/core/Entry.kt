package dev.metinkale.prayertimes.core

import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.utils.normalize
import dev.metinkale.prayertimes.core.utils.roundLatLng
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable


@Serializable
data class Entry(
    val id: String,
    val lat: Double?,
    val lng: Double?,
    val country: String,
    val names: List<Map<String, String>>,
    val source: Source,
    val timeZone: TimeZone? = null
) {
    val normalizedNames by lazy {
        this.names.flatMap { it.values.flatMap { it.normalize().split(' ') } }.filter { it.isNotEmpty() }
            .distinct()
    }

    val localizedNames by lazy {
        names.map { map ->
            Configuration.languages.firstNotNullOfOrNull { map[it] } ?: map.values.first()
        }
    }

    val localizedName by lazy { localizedNames.firstOrNull() ?: "" }

    val localizedRegion by lazy { localizedNames.drop(1).joinToString(" - ") }


    companion object {
        val DELIM = '\t'
        fun decodeFromString(source: Source, line: String): Entry = line.split(DELIM).let {
            Entry(
                id = it[0],
                lat = it[1].ifBlank { null }?.toDouble()?.roundLatLng(),
                lng = it[2].ifBlank { null }?.toDouble()?.roundLatLng(),
                country = it[3],
                names = it.drop(4).parseNames(),
                source = source
            )
        }

        private fun List<String>.parseNames(): List<Map<String, String>> = map {
            if (!it.contains('=')) mapOf("" to it)
            else it.split(";").map {
                it.split("=").let { it[0] to it[1] }
            }.toMap()
        }

    }
}

