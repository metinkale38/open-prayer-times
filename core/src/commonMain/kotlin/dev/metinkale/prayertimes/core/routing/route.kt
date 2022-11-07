package dev.metinkale.prayertimes.core.routing

suspend fun <T> Request.route(vararg routes: (Pair<String, suspend Request.() -> Response<T>>)): Response<T> {
    routes.forEach { (route, build) ->
        val paths = route.trim('/').split("/")
        if (paths.indices.all { paths[it] == path[it] }) {
            return build.invoke(dropPath(paths.size))
        }
    }
    return Response(404)
}