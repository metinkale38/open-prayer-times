package dev.metinkale.prayertimes.core.routing

data class Request(
    val path: List<String>,
    val params: Map<String, String>,
    val languages: List<String> = listOf("en", "tr", "de")
) {
    fun dropPath(count: Int): Request = copy(path = path.drop(count))

    companion object {
        fun fromURL(path: String): Request {
            val params =
                path.substringAfter("?").split("&").map { it.substringBefore("=") to it.substringAfter("=") }.toMap()
            val parts = path.substringBeforeLast("?").trim('/').split("/").toList()
            return Request(parts, params)
        }
    }
}