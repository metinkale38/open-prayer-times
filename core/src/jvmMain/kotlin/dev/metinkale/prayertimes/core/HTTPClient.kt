package dev.metinkale.prayertimes.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


internal actual object HttpClient {
    actual suspend fun get(url: String, build: HTTPClientBuilder.() -> Unit): String = send("GET", url, build)

    actual suspend fun post(url: String, build: HTTPClientBuilder.() -> Unit): String = send("POST", url, build)

    suspend fun send(method: String, url: String, build: HTTPClientBuilder.() -> Unit): String =
        withContext(Dispatchers.IO) {
            val builder = HTTPClientBuilder().apply(build)
            val conn: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            try {
                conn.requestMethod = method
                builder.headers.forEach { (k, v) -> conn.setRequestProperty(k, v) }
                builder.body?.let { body ->
                    conn.doOutput = true
                    conn.outputStream.use {
                        it.bufferedWriter().apply {
                            write(body)
                            flush()
                        }
                    }
                }


                try {
                    conn.inputStream.bufferedReader().readText()
                } catch (e: Throwable) {
                    conn.errorStream.bufferedReader().readText().let { System.err.println(it) }
                    throw e
                }
            } finally {
                conn.disconnect()
            }
        }

}