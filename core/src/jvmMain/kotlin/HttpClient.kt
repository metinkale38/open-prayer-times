package dev.metinkale.prayertimes.core

import io.ktor.client.*
import io.ktor.client.engine.cio.*

internal actual val httpClient: HttpClient = HttpClient(CIO)