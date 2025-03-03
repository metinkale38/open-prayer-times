package dev.metinkale.prayertimes.providers.sources

import dev.metinkale.prayertimes.providers.DayTimes
import dev.metinkale.prayertimes.providers.httpClient
import dev.metinkale.prayertimes.providers.sources.features.CityListFeature
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime


internal object Diyanet : Source, CityListFeature {
    override val name: String = "Diyanet"


    override suspend fun getDayTimes(key: String): List<DayTimes> {
        var result = httpClient.get("https://namazvakitleri.diyanet.gov.tr/tr-TR/$key").bodyAsText()

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
