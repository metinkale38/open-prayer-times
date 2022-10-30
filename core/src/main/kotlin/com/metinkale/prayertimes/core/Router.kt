package com.metinkale.prayertimes.core

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes


val router: HttpHandler = routes(
    "/search/{query}" bind Method.GET to {
        val query = it.path("query")!!
        Response.invoke(Status.OK).body(Cities.search(query))
    },
    "/location/{lat}/{lng}" bind Method.GET to {
        val lat = it.path("lat")!!.toDouble()
        val lng = it.path("lng")!!.toDouble()
        Response.invoke(Status.OK).body(Cities.search(lat, lng))
    }
)


