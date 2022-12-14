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

    fun name(vararg language: String): String = language.firstNotNullOfOrNull { l ->
        names.first().let { it[l] }
    } ?: names.first().values.first()

    fun encodeToString() = buildString {
        append(id)
        append(DELIM)
        append(lat.takeIf { it != 0.0 }?.roundLatLng() ?: "")
        append(DELIM)
        append(lng.takeIf { it != 0.0 }?.roundLatLng() ?: "")
        append(DELIM)
        append(country)
        append(DELIM)
        names.joinToString("$DELIM") {
            it.values.distinct().takeIf { it.size == 1 }?.first() ?: run {
                it.toList().joinToString(";") { (key, value) -> "$key=$value" }
            }
        }.let { append(it) }
    }

    suspend fun withTimeZone(): Entry =
        if (lat != null && lng != null) copy(timeZone = Geocoder.getTimeZone(lat, lng)) else this

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

