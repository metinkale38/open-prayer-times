package dev.metinkale.prayertimes.cloudfunctions

import dev.metinkale.prayertimes.core.router
import org.http4k.serverless.GoogleCloudHttpFunction

class App : GoogleCloudHttpFunction(router)