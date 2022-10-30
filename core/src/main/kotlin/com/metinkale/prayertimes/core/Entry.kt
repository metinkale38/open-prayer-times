package com.metinkale.prayertimes.core

import com.metinkale.prayertimes.core.sources.Source
import kotlinx.serialization.Serializable

@Serializable
data class Entry(
    val id: String,
    val lat: Double?,
    val lng: Double?,
    val country: String,
    val names: List<Map<String, String>>,
    val source: Source
) {
    val name get() = names[0].values.first()

    fun encodeToString() = buildString {
        append(id)
        append(DELIM)
        append(lat.takeIf { it != 0.0 } ?: "")
        append(DELIM)
        append(lng.takeIf { it != 0.0 } ?: "")
        append(DELIM)
        append(country)
        append(DELIM)
        names.joinToString("$DELIM") {
            it.values.distinct().takeIf { it.size == 1 }?.first() ?: run {
                it.toList().joinToString(";") { (key, value) -> "$key=$value" }
            }
        }.let { append(it) }
    }

    companion object {
        val DELIM = '\t'
        fun decodeFromString(source: Source, line: String): Entry = line.split(DELIM).let {
            Entry(
                id = it[0],
                lat = it[1].toDoubleOrNull(),
                lng = it[2].toDoubleOrNull(),
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

