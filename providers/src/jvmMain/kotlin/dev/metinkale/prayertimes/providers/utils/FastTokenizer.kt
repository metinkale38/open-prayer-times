package dev.metinkale.prayertimes.providers.utils

class FastTokenizer(private val str: String, private val delim: Char) {
    private var start = 0

    fun next(): String {
        var to = str.indexOf(delim, start)
        if (to < 0) {
            to = str.length
        }
        val from = start
        start = to + 1
        return str.substring(from, to)
    }

    fun nextDouble(): Double {
        val str = next()
        return if (str.isEmpty()) 0.0 else str.toDouble()
    }

    fun nextInt(): Int {
        val str = next()
        return if (str.isEmpty()) 0 else str.toInt()
    }

    fun hasNext() = start < str.length

}
