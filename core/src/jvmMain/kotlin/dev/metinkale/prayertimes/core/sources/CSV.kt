package dev.metinkale.prayertimes.core.sources

import dev.metinkale.prayertimes.core.DayTimes
import dev.metinkale.prayertimes.core.httpClient
import dev.metinkale.prayertimes.core.utils.now
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.io.File

internal object CSV : Source {
    override val name: String = "CSV"


    override suspend fun getDayTimes(key: String): List<DayTimes> {
        val year = LocalDate.now().year
        val content = if (key.startsWith("http://") || key.startsWith("https://")) {
            httpClient.get(key).bodyAsText().lineSequence()
        } else {
            File(key).readText().lineSequence()
        }

        return content.map { it.replace("\"", "") }.mapNotNull {
            runCatching {
                val rows = it.split(';', ',', ';', '\t', ' ').filter { it.isNotBlank() }

                val date = rows[0].let {
                    var parts = it.split(':', '-', '_').map { it.toInt() }

                    if (parts.size == 3) {
                        if (parts[0] < parts[2]) parts = parts.reversed()
                        LocalDate(parts[0], parts[1], parts[2])
                    } else if (parts.size == 2) {
                        LocalDate(year, parts[1], parts[0])
                    } else {
                        return@mapNotNull null
                    }
                }

                rows.drop(1).map {
                    val parts = it.split(":", "-", "_").map { it.toInt() }
                    LocalTime(parts[0], parts[1])
                }.let {
                    DayTimes(
                        date = date,
                        fajr = it[0],
                        sun = it[1],
                        dhuhr = it[2],
                        asr = it[3],
                        maghrib = it[4],
                        ishaa = it[5],
                        sabah = it.getOrNull(6),
                        asrHanafi = it.getOrNull(7)
                    )
                }
            }.getOrNull()
        }.toList()
    }
}