package com.metinkale.prayertimes.cloudfunctions

import com.metinkale.prayertimes.core.router
import org.http4k.serverless.GoogleCloudHttpFunction

class App : GoogleCloudHttpFunction(router)