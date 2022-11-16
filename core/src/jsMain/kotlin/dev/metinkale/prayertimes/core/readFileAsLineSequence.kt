package dev.metinkale.prayertimes.core

import nodejs.__dirname


var pathPrefix: String? = null

actual fun readFileAsLineSequence(filePath: String): Sequence<String> = sequence {
    @Suppress("UNUSED_VARIABLE") val path = (pathPrefix ?: __dirname) + "/" + filePath
    val liner = js("new (require(\"n-readlines\"))(path)")
    var line: dynamic
    while (true) {
        line = liner.next()
        if (line == false) break
        else yield(line.toString("utf-8") as String)
    }
}
