package dev.metinkale.prayertimes.core

internal external fun require(name: String): dynamic
internal external val __dirname: dynamic
internal fun obj(vararg fields: Pair<String, dynamic>): dynamic {
    val obj = js("{}")
    fields.forEach { (key, value) ->
        obj[key] = value
    }
    return obj
}
