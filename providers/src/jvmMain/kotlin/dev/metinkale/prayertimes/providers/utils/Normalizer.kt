package dev.metinkale.prayertimes.providers.utils


internal fun String.normalize(): String = String(map(Char::normalize).toCharArray()).trim()

internal fun Char.normalize(): Char = when (this) {
    in 'A'..'Z' -> this + 0x20
    in 'a'..'z' -> this
    'é', 'è', 'ê', 'ë', 'È', 'É', 'Ë', 'Ê' -> 'e'
    'Ç', 'ç' -> 'c'
    'Ğ', 'ğ' -> 'g'
    'ı', 'İ', 'ï', 'î', 'Ï', 'Î' -> 'i'
    'Ö', 'ö', 'Ô' -> 'o'
    'Ş', 'ş' -> 's'
    'Ä', 'ä', 'à', 'â', 'À', 'Â' -> 'a'
    'ü', 'Ü', 'û', 'ù', 'Û', 'Ù' -> 'u'
    else -> this
}