
import dev.metinkale.prayertimes.core.Router
import dev.metinkale.prayertimes.core.routing.Request
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.Promise
import express.Request as JRequest
import express.Response as JResponse

@DelicateCoroutinesApi
@JsExport
fun doRequest(req: JRequest, res: JResponse): Promise<dynamic> = GlobalScope.promise {
    val langs: List<String>? =
        (req.headers["accept-language"] as? String)?.split(",")?.map { it.substringBefore(',').substringBefore('-') }
    val request = Request.fromURL(req.originalUrl).let {
        if (langs?.isEmpty() != false) it
        else it.copy(languages = langs)
    }
    val response = Router.route(request)
    res.status(response.status)
    res.type(response.contentType ?: "")
    res.send(response.body ?: "")
}
