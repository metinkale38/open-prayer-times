package dev.metinkale.prayertimes.providers.sources

import dev.metinkale.prayertimes.providers.DayTimes
import dev.metinkale.prayertimes.providers.cached
import dev.metinkale.prayertimes.providers.httpClient
import dev.metinkale.prayertimes.providers.sources.features.CityListFeature
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

internal object Diyanet : Source, CityListFeature {
    override val name: String = "Diyanet"

    override suspend fun getDayTimes(key: String): List<DayTimes> = cached(key) {
        var result = httpClient.post("https://namazvakti.diyanet.gov.tr/wsNamazVakti.svc") {
            contentType(ContentType.parse("text/xml; charset=utf-8"))
            header("SOAPAction", "http://tempuri.org/IwsNamazVakti/AylikNamazVakti")
            setBody(
                "<v:Envelope xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:d=\"http://www.w3.org/2001/XMLSchema\" xmlns:c=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:v=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<v:Header /><v:Body>" + "<AylikNamazVakti xmlns=\"http://tempuri.org/\" id=\"o0\" c:root=\"1\">" +
                        "<IlceID i:type=\"d:int\">" + key + "</IlceID>" +
                        "<username i:type=\"d:string\">namazuser</username>" + "<password i:type=\"d:string\">NamVak!14</password>" +
                        "</AylikNamazVakti></v:Body></v:Envelope>"
            )
        }.bodyAsText()

        result = result.substring(result.indexOf("<a:NamazVakti>") + 14)
        result = result.substring(0, result.indexOf("</AylikNamazVaktiResult>"))
        val days = result.split("</a:NamazVakti><a:NamazVakti>".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        days.map { day ->
            val parts = day.split("><a:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val times = arrayOfNulls<String>(6)
            var date: String? = null
            for (part: String in parts) {
                if (!part.contains(">")) continue
                var name = part.substring(0, part.indexOf('>'))
                if (name.contains(":")) name = name.substring(name.indexOf(':') + 1)
                var content = part.substring(part.indexOf('>') + 1)
                content = content.substring(0, content.indexOf('<'))
                when (name) {
                    "Imsak" -> times[0] = content
                    "Gunes" -> times[1] = content
                    "Ogle" -> times[2] = content
                    "Ikindi" -> times[3] = content
                    "Aksam" -> times[4] = content
                    "Yatsi" -> times[5] = content
                    "MiladiTarihKisa" -> date = content
                }
            }
            val d = date!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val ld = LocalDate(d[2].toInt(), d[1].toInt(), d[0].toInt())

            DayTimes(
                ld,
                fajr = LocalTime.parse(times[0]!!),
                sun = LocalTime.parse(times[1]!!),
                dhuhr = LocalTime.parse(times[2]!!),
                asr = LocalTime.parse(times[3]!!),
                maghrib = LocalTime.parse(times[4]!!),
                ishaa = LocalTime.parse(times[5]!!)
            )
        }
    }


}
