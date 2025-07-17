package dev.metinkale.prayertimes.providers.utils


internal fun String.normalize(): String = String(map(Char::normalize).filter { it != '\u0000' }.toCharArray()).trim()

internal fun Char.normalize(): Char = when (this) {
    in 'A'..'Z' -> (this + 0x20)
    in 'a'..'z' -> this
    in '0'..'9' -> this
    'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'à', 'á', 'â', 'ã', 'ä', 'å', 'ā' -> 'a'
    'Ç', 'ç', 'č' -> 'c'
    'È', 'É', 'Ê', 'Ë', 'è', 'é', 'ê', 'ë', 'ě', 'ė' -> 'e'
    'Ì', 'Í', 'Î', 'Ï', 'ì', 'í', 'î', 'ï', 'İ', 'ı' -> 'i'
    'Ñ', 'ñ', 'ń' -> 'n'
    'Ò', 'Ó', 'Ô', 'Õ', 'Ö', 'Ø', 'ò', 'ó', 'ô', 'õ', 'ö', 'ø' -> 'o'
    'Š', 'š', 'Ş', 'ş' -> 's'
    'Ù', 'Ú', 'Û', 'Ü', 'ù', 'ú', 'û', 'ü' -> 'u'
    'Ý', 'ý', 'ÿ' -> 'y'
    'Ž', 'ž' -> 'z'
    'Ğ', 'ğ' -> 'g'
    'ß' -> 's'
    'Þ', 'þ' -> 'p'
    'ř' -> 'r'
    '.', '-', ' ', '(', ')', '\u0027', ',', '/', '\\' -> '\u0000'
    else -> this
}