package dev.metinkale.prayertimes.core


internal expect object HttpClient {
    suspend fun get(url: String, build: HTTPClientBuilder.() -> Unit = {}): String
    suspend fun post(url: String, build: HTTPClientBuilder.() -> Unit = {}): String
}

internal class HTTPClientBuilder {
    var body: String? = null
    val headers: MutableMap<String, String> = mutableMapOf()
    fun header(key: String, value: String) {
        headers[key] = value
    }

    var contentType: String?
        get() = headers["Content-Type"]
        set(v) {
            if (v != null) header("Content-Type", v) else headers.remove("Content-Type")
        }
}
