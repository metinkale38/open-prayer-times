package dev.metinkale.prayertimes.core

internal actual fun readFileAsLineSequence(filePath: String): Sequence<String> = sequence {
    @Suppress("UNUSED_VARIABLE") val path = __dirname + "/" + filePath
    val liner = js("new (require(\"n-readlines\"))(path)")
    var line: dynamic
    while (true) {
        line = liner.next()
        if (line == false) break
        else yield(line.toString("utf-8") as String)
    }
}
