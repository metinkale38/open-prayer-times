package dev.metinkale.prayertimes.core.router


/* This is a very lightweight and simple HTTP Router inspired by http4k
 * The idea of writing a HttpHandler originally came from running the service as node.js. It worked, but was very slow
 *
 * The js-Target got removed, so there is no reason anymore not to use http4k. TODO migrate to http4k
 */

interface HttpHandler<T> {
    suspend operator fun invoke(request: Request): Response<T>

    suspend operator fun invoke(
        method: Method,
        path: String,
        params: Map<String, String>,
        headers: Map<String, String>
    ) = invoke(Request(method, path, params, headers))


    fun <R> mapBody(build: (T) -> R): HttpHandler<R> = HttpHandler {
        invoke(this).let { Response(it.status, it.body?.run(build), it.error, it.contentType) }
    }

    fun withContentType(contentType: String): HttpHandler<T> = HttpHandler {
        invoke(this).let { Response(it.status, it.body, it.error, contentType) }
    }


    companion object {
        operator fun <T> invoke(build: suspend Request.() -> Response<T>) =
            object : HttpHandler<T> {
                override suspend fun invoke(request: Request): Response<T> = build.invoke(request)
            }
    }
}

fun HttpHandler<String>.withContentTypeJson() = withContentType("application/json")


class Router<T> : MutableList<Router.Route<T>> by mutableListOf() {
    data class Route<T>(val method: Method, val path: String, val router: HttpHandler<T>)

    infix fun String.GET(handler: HttpHandler<T>): Unit =
        add(Route(Method.GET, this, handler)).let { }

    infix fun String.POST(handler: HttpHandler<T>): Unit =
        add(Route(Method.POST, this, handler)).let { }

    infix fun String.PUT(handler: HttpHandler<T>): Unit =
        add(Route(Method.PUT, this, handler)).let { }

    infix fun String.DELETE(handler: HttpHandler<T>): Unit =
        add(Route(Method.DELETE, this, handler)).let { }

    infix fun String.PATCH(handler: HttpHandler<T>): Unit =
        add(Route(Method.PATCH, this, handler)).let { }

    companion object {
        operator fun <T> invoke(build: Router<T>.() -> Unit): HttpHandler<T> = HttpHandler {
            Router<T>().apply(build).filter { it.method == method }
                .firstNotNullOfOrNull { (_, route, build) ->
                    val routerParts = route.trim('/').split("/")
                    if (routerParts.indices.all { routerParts[it] == pathParts[it] }) {
                        build.invoke(dropPathParts(routerParts.size))
                    } else null
                } ?: Response(404)
        }
    }
}


data class Response<T>(
    val status: Int,
    val body: T? = null,
    val error: String? = null,
    val contentType: String? = null
)

enum class Method { GET, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH }
data class Request(
    val method: Method,
    val path: String,
    val params: Map<String, String>,
    val headers: Map<String, String>
) {
    val pathParts get() = path.trim('/').ifBlank { null }?.split("/") ?: emptyList()
    fun dropPathParts(drop: Int) = copy(path = pathParts.drop(drop).joinToString("/"))
}
