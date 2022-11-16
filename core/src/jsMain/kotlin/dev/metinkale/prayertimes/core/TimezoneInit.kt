package dev.metinkale.prayertimes.core


internal object TimezoneInit {
    private val jsJodaTz = JsJodaTimeZoneModule
    fun init() {}
}

@JsModule("@js-joda/timezone")
@JsNonModule
internal external object JsJodaTimeZoneModule

