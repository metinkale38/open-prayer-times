package nodejs

fun obj(vararg fields: Pair<String, dynamic>): dynamic {
    val obj = js("{}")
    fields.forEach { (key, value) ->
        obj[key] = value
    }
    return obj
}
