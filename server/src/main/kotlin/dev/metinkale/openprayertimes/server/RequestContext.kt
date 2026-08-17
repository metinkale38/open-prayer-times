package dev.metinkale.openprayertimes.server

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

data class RequestContext(
    val languages: List<String>
) : CoroutineContext.Element {
    override val key: CoroutineContext.Key<*> = RequestCContextKey

    companion object {
        private val RequestCContextKey = object : CoroutineContext.Key<RequestContext> {}

        suspend fun current(): RequestContext? {
            return kotlin.coroutines.coroutineContext[RequestCContextKey]
        }

        suspend fun <T> withLanguages(languages: List<String>, block: suspend CoroutineScope.() -> T): T {
            val context = RequestContext(languages)
            return withContext(context, block)
        }
    }
}

