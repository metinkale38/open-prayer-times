package dev.metinkale.openprayertimes.server.api

import dev.metinkale.openprayertimes.server.feature.ListableProvider
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val dateRegex = Regex("""^\d{4}-\d{2}-\d{2}$""")
fun Route.listRoutes() {
    get("/{path...}") {
        val path = call.parameters.getAll("path")?.map { it.replace("+", " ") }
            ?: throw NotFoundException()

        val last = path.lastOrNull()
        when {
            last == "today" -> handleToday(path.dropLast(1))
            last == "json" -> handleJSON(path.dropLast(1))
            last == "csv" -> handleCSV(path.dropLast(1))
            last != null && dateRegex.matches(last) -> handleDate(path.dropLast(1), last)
            else -> {
                val children = ListableProvider.getChildren(path)

                if (children.isEmpty()) {
                    call.respond(ListableProvider.getCity(path) ?: throw NotFoundException())
                } else {
                    call.respond(children)
                }
            }

        }
    }
}
