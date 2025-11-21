package dev.metinkale.openprayertimes

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.userAgent

internal val httpClient: HttpClient = HttpClient(CIO)