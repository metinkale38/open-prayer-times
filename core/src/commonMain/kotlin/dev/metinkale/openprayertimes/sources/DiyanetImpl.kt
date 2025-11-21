package dev.metinkale.openprayertimes.sources

import dev.metinkale.openprayertimes.DayTimes
import dev.metinkale.openprayertimes.httpClient
import dev.metinkale.openprayertimes.sources.features.IterableSource
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.HttpHeaders
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class DiyanetImpl internal constructor() : Source, IterableSource {
    override val name: String = "Diyanet"


    override suspend fun getDayTimes(key: String): List<DayTimes> {
        // TODO migrate to https://awqatsalah.diyanet.gov.tr with server side caching
        val response = httpClient.get("https://namazvakitleri.diyanet.gov.tr/tr-TR/$key/sehir-icin-namaz-vakti") {
            header(
                HttpHeaders.Accept,
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8"
            )
            header(HttpHeaders.AcceptLanguage, "de-DE,de;q=0.6")
            header(HttpHeaders.Connection, "keep-alive")
            header(
                HttpHeaders.UserAgent,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36"
            )
        }

        var result = response.bodyAsText()

        result = result.substringAfter("id=\"tab-2\"").substringBefore("</table>")

        return result.split("</tr>").filter { it.contains("td>") }.map {
            val day = it.split("</td>").filter { it.contains("td>") }.map { it.substringAfter("td>") }

            DayTimes(
                LocalDate.parse(
                    day[0].substringBeforeLast(" ").split(" ").reversed().joinToString("-")
                        .replace("Ocak", "01")
                        .replace("Şubat", "02")
                        .replace("Mart", "03")
                        .replace("Nisan", "04")
                        .replace("Mayıs", "05")
                        .replace("Haziran", "06")
                        .replace("Temmuz", "07")
                        .replace("Ağustos", "08")
                        .replace("Eyl&#252;l", "09")
                        .replace("Ekim", "10")
                        .replace("Kasım", "11")
                        .replace("Aralık", "12")
                ),
                LocalTime.parse(day[2]),
                LocalTime.parse(day[3]),
                LocalTime.parse(day[4]),
                LocalTime.parse(day[5]),
                LocalTime.parse(day[6]),
                LocalTime.parse(day[7])
            )
        }
    }
}
