package dev.metinkale.prayertimes.cloudfunction

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import dev.metinkale.prayertimes.core.router.HttpHandler
import dev.metinkale.prayertimes.core.router.Method
import dev.metinkale.prayertimes.core.router.coreRouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Api : HttpFunction {
    override fun service(request: HttpRequest?, response: HttpResponse?): Unit = runBlocking {
        withContext(Dispatchers.Default) {
            request?.let { request -> response?.let { response -> coreRouter.handle(request, response) } }
        }
    }

}

suspend fun HttpHandler<String>.handle(request: HttpRequest, response: HttpResponse) {
    request.run {
        invoke(
            Method.valueOf(method),
            path,
            queryParameters.mapValues { it.value.first() },
            headers.mapValues { it.value.first() })
    }.let {
        response.setStatusCode(it.status)
        response.setContentType(it.contentType)
        it.body?.let { response.writer.write(it) }
    }
}
