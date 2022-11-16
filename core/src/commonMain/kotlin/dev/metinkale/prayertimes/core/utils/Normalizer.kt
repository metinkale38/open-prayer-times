package dev.metinkale.prayertimes.core.utils

internal fun String.normalize(): String = StringBuilder(length).apply {
    this@normalize.forEach { char ->
        append(
            when (char) {
                in 'A'..'Z' -> char + 0x20
                in 'a'..'z' -> char
                'é', 'è', 'ê', 'ë', 'È', 'É', 'Ë', 'Ê' -> 'e'
                'Ç', 'ç' -> 'c'
                'Ğ', 'ğ' -> 'g'
                'ı', 'İ', 'ï', 'î', 'Ï', 'Î' -> 'i'
                'Ö', 'ö', 'Ô' -> 'o'
                'Ş', 'ş' -> 's'
                'Ä', 'ä', 'à', 'â', 'À', 'Â' -> 'a'
                'ü', 'Ü', 'û', 'ù', 'Û', 'Ù' -> 'u'
                else -> ' '
            }
        )
    }
}.toString()