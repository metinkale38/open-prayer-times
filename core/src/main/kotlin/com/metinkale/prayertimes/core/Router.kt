package com.metinkale.prayertimes.core

import com.metinkale.prayertimes.core.sources.features.ByLocationFeature
import com.metinkale.prayertimes.core.sources.features.SearchFeature
import com.metinkale.prayertimes.core.sources.Source
import kotlinx.coroutines.runBlocking
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes


val router: HttpHandler = routes(
    "/search/{query}" bind Method.GET to {
        runBlocking {
            val query = it.path("query")!!
            val geo = Geocoder.search(query, "en")
            Response(Status.OK).body(
                Source.values().mapNotNull { it as? SearchFeature }.map { it.search(query, geo) })
        }
    },
    "/location/{lat};{lng}" bind Method.GET to {
        runBlocking {
            val lat = it.path("lat")!!.toDouble()
            val lng = it.path("lng")!!.toDouble()
            val geo = Geocoder.reverse(lat, lng, "en")

            Response(Status.OK).body(
                Source.values().mapNotNull { it as? ByLocationFeature }.map { it.search(geo) }
            )
        }
    },
    "/times/{source}/{id}" bind Method.GET to {
        val id = it.path("id")
        val source = Source.valueOf(it.path("source")!!)
        source?.let {
            id?.let {
                Response(Status.OK).body(runBlocking { source.getDayTimes(id) })
            }
        } ?: Response(Status.NOT_FOUND)
    }

)


