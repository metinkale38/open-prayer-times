package dev.metinkale.prayertimes.core.utils


fun String.dropRows(count: Int): String {
    var index = -1
    (0 until count).forEach {
        index = indexOf('\t', index + 1)
    }
    return substring(index + 1)
}