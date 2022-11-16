import dev.metinkale.prayertimes.core.obj
import dev.metinkale.prayertimes.core.router.Method
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.Json
import dev.metinkale.prayertimes.core.router.coreRouter as router

@JsExport
fun coreRouter(
    method: String,
    path: String,
    params: Json,
    headers: Json,
): dynamic = GlobalScope.promise {
    router.invoke(Method.valueOf(method), path, params.asMap(), headers.asMap()).let {
        obj(
            "status" to it.status,
            "body" to it.body,
            "error" to it.error,
            "contentType" to it.contentType
        )
    }
}

@Suppress("UNUSED_ANONYMOUS_PARAMETER", "UNCHECKED_CAST")
private fun Json.asMap(): Map<String, String> = object : AbstractMap<String, String>() {
    override val entries: Set<Map.Entry<String, String>>
        get() = this@asMap.let { json -> js("Objects.entries(json)") as? Array<dynamic> }?.map {
            object : Map.Entry<String, String> {
                override val key: String = it[0]
                override val value: String = it[1] as String
            }
        }?.toSet() ?: emptySet()

    override fun get(key: String): String? = this@asMap.get(key) as? String
}
