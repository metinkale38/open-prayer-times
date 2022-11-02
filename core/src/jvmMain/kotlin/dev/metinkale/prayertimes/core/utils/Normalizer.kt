package dev.metinkale.prayertimes.core.utils

import java.util.*

fun String.normalize(): String {
    val builder = StringBuilder()
    for (c in toCharArray()) {
        if (c.code in 0x41..0x5A) { //A-Z
            builder.append((c.code + 0x20).toChar())
        } else if (c.code in 0x61..0x7A) { //a-z
            builder.append(c)
        } else {
            when (c) {
                'é', 'è', 'ê', 'ë', 'È', 'É', 'Ë', 'Ê' -> builder.append("e")
                'Ç', 'ç' -> builder.append("c")
                'Ğ', 'ğ' -> builder.append("g")
                'ı', 'İ', 'ï', 'î', 'Ï', 'Î' -> builder.append("i")
                'Ö', 'ö', 'Ô' -> builder.append("o")
                'Ş', 'ş' -> builder.append("s")
                'Ä', 'ä', 'à', 'â', 'À', 'Â' -> builder.append("a")
                'ü', 'Ü', 'û', 'ù', 'Û', 'Ù' -> builder.append("u")
                else -> builder.append(c)
            }
        }
    }
    return builder.toString()
}