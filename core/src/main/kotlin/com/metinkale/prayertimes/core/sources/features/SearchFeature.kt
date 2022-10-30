package com.metinkale.prayertimes.core.sources.features

import com.metinkale.prayertimes.core.Entry
import com.metinkale.prayertimes.core.Geocoder
import com.metinkale.prayertimes.core.utils.normalize
import kotlin.math.abs

interface SearchFeature : CityListFeature, ByLocationFeature {
    suspend fun search(query: String, geolocation: Geocoder.GeoLocation?): Entry? {
        val q: String = query.normalize()
        var nameMatch: Entry? = null
        var locMatch: Entry? = null
        getCities().forEach { entry ->
            val norm = entry.name
            val contains = norm.contains(q)
            if (contains) {
                if (nameMatch == null) {
                    nameMatch = entry
                } else if (geolocation != null && entry.lat != null && entry.lng != null) {
                    val latDist: Double = abs(geolocation.lat - entry.lat)
                    val lngDist: Double = abs(geolocation.lon - entry.lng)
                    if (nameMatch!!.lat != null && nameMatch!!.lng != null && latDist + lngDist < abs(geolocation.lat - nameMatch!!.lat!!) + abs(
                            geolocation.lon - nameMatch!!.lng!!
                        )
                    ) {
                        nameMatch = entry
                    }
                }
            } else if (nameMatch == null && geolocation != null && entry.lat != null && entry.lng != null && geolocation.lat != 0.0 && geolocation.lon != 0.0) {
                val latDist: Double = abs(geolocation.lat - entry.lat)
                val lngDist: Double = abs(geolocation.lon - entry.lng)
                if (locMatch == null) {
                    if (latDist < 2 && lngDist < 2) locMatch = entry
                } else if (locMatch!!.lat != null && locMatch!!.lng != null) {
                    if (latDist + lngDist < abs(geolocation.lat - locMatch!!.lat!!) + abs(geolocation.lon - locMatch!!.lng!!)) {
                        locMatch = entry
                    }
                }
            }

        }

        return nameMatch ?: locMatch
    }

    override fun search(geolocation: Geocoder.GeoLocation): Entry? {
        val lat = geolocation.lat
        val lng = geolocation.lon
        var last: Entry? = null
        getCities().filter { it.lat != null && it.lng != null }.forEach { entry ->
            val latDist = abs(lat - entry.lat!!)
            val lngDist = abs(lng - entry.lng!!)
            if (last == null) {
                if (latDist < 2 && lngDist < 2) last = entry
            } else {
                if (latDist + lngDist < abs(lat - last!!.lat!!) + abs(lng - last!!.lng!!)) {
                    last = entry
                }
            }
        }

        return last
    }

}

