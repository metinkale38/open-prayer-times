package dev.metinkale.prayertimes.core

import io.ktor.client.*
import io.ktor.client.engine.js.*

internal actual val httpClient: HttpClient = HttpClient(Js)