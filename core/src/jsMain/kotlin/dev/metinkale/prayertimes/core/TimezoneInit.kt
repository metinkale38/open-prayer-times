package dev.metinkale.prayertimes.core


object TimezoneInit {
    private val jsJodaTz = JsJodaTimeZoneModule
    fun init() {}
}

@JsModule("@js-joda/timezone")
@JsNonModule
external object JsJodaTimeZoneModule

