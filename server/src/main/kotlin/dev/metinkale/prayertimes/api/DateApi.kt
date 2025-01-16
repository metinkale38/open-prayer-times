package dev.metinkale.prayertimes.api

import dev.metinkale.hijri.HijriDate
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Date(
    val date: String,
    val hijri: String,
    val event: String? = null
) {
    companion object {
        fun from(date: LocalDate): Date {
            val hijri = HijriDate.fromLocalDate(date)
            return Date(date.toString(), hijri.toString(), hijri.getEvent()?.name)
        }

        fun from(hijri: HijriDate): Date {
            val date = hijri.toLocalDate()
            return Date(date.toString(), hijri.toString(), hijri.getEvent()?.name)
        }
    }
}

fun Route.dateApi() {
    route("/date") {
        handle {
            call.respond(Date.from(LocalDate.now()))
        }
    }
    listOf("hijri" to true, "date" to false).forEach { (route, isHijri) ->
        route("/$route/{date}") {
            handle {
                val date = call.parameters["date"] ?: throw NotFoundException()
                val parts = date.split("-")
                when (parts.size) {
                    3 -> {
                        val year = parts[0].toIntOrNull() ?: throw NotFoundException()
                        val month = parts[1].toIntOrNull() ?: throw NotFoundException()
                        val day = parts[2].toIntOrNull() ?: throw NotFoundException()

                        val entity = if (isHijri) Date.from(HijriDate.of(year, month, day))
                        else Date.from(LocalDate.of(year, month, day))
                        call.respond(entity)
                    }

                    1 -> {
                        val year = parts[0].toIntOrNull() ?: throw NotFoundException()
                        val events = if (isHijri) HijriDate.getEventsForHijriYear(year)
                        else HijriDate.getEventsForGregYear(year)

                        val response = events.toList().map { (date, event) ->
                            Date(
                                date.toLocalDate().toString(),
                                date.toString(),
                                event.name
                            )
                        }
                        call.respond(response)
                    }

                    else -> throw NotFoundException()
                }
            }
        }
    }
}