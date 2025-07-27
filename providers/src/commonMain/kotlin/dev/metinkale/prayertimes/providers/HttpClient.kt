package dev.metinkale.prayertimes.providers

import io.ktor.client.*
import io.ktor.client.engine.cio.*

internal val httpClient: HttpClient = HttpClient(CIO)