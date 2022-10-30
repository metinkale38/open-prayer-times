package com.metinkale.prayertimes.core

import com.metinkale.prayertimes.core.utils.normalize
import kotlinx.coroutines.runBlocking
import kotlin.math.abs

object Cities {
     fun readTSV(source: Source): Sequence<Entry> {
        return Cities::class.java.getResourceAsStream("/tsv/" + source.tsv)!!.bufferedReader(Charsets.UTF_8)
            .lineSequence().map { line ->
                if(line.contains("d800")) println(line)
                Entry.decodeFromString(line)
            }
    }

    /*
    fun list(source: Source): List<Entry> = list(source.id)

    fun list(id: Int): List<Entry> {
        if (id == 0) {
            return Source.values().map { Entry(0, it.id, it.name, it.name, 0.0, 0.0, it) }
        }
        val sourceId = (id shl (32 - 4) shr (32 - 4))
        val source = Source.values().find { it.id == sourceId } ?: return emptyList()
        return readTSV(source).filter { it.parent == id }.toList()
    }*/


    fun search(lat: Double, lng: Double): List<Entry> {
        val map: MutableMap<Source, Entry> = mutableMapOf()
        Source.values().forEach { s ->
            readTSV(s).filter { it.lat != null && it.lng != null }.forEach { entry ->
                val e: Entry? = map.getOrDefault(s, null)
                val latDist = abs(lat - entry.lat!!)
                val lngDist = abs(lng - entry.lng!!)
                if (e == null) {
                    if (latDist < 2 && lngDist < 2) map[s] = entry
                } else {
                    if (latDist + lngDist < abs(lat - e.lat!!) + abs(lng - e.lng!!)) {
                        map[s] = entry
                    }
                }
            }
        }
        return map.values.toList()
    }

    fun search(query: String): List<Entry> = runBlocking {

        val geolocation = Geocoder.search(query, "tr")


        val q: String = query.normalize()
        val name: MutableMap<Source, Entry> = mutableMapOf()
        val pos: MutableMap<Source, Entry> = mutableMapOf()
        Source.values().forEach { s ->
            readTSV(s).forEach { entry ->
                val norm = entry.name
                val contains = norm.contains(q)
                if (!contains && !name.containsKey(s) && geolocation != null && entry.lat != null && entry.lng != null && geolocation.lat != 0.0 && geolocation.lon != 0.0) {
                    val e: Entry? = pos.getOrDefault(s, null)
                    val latDist: Double = abs(geolocation.lat - entry.lat)
                    val lngDist: Double = abs(geolocation.lon - entry.lng)
                    if (e == null) {
                        if (latDist < 2 && lngDist < 2) pos[s] = entry
                    } else if (e.lat != null && e.lng != null) {
                        if (latDist + lngDist < abs(geolocation.lat - e.lat) + abs(geolocation.lon - e.lng)) {
                            pos[s] = entry
                        }
                    }
                }
                if (contains) {
                    val e: Entry? = name.getOrDefault(s, null)
                    if (e == null) {
                        name[s] = entry
                    } else if (geolocation != null && entry.lat != null && entry.lng != null) {
                        val latDist: Double = abs(geolocation.lat - entry.lat)
                        val lngDist: Double = abs(geolocation.lon - entry.lng)
                        if (e.lat != null && e.lng != null && latDist + lngDist < abs(geolocation.lat - e.lat) + abs(
                                geolocation.lon - e.lng
                            )
                        ) {
                            name[s] = entry
                        }
                    }
                }
            }
        }
        val items: MutableList<Entry> = ArrayList(name.values)
        for ((s,e) in pos.entries) {
            if (!name.containsKey(s)) items.add(e)
        }
        items
    }
}