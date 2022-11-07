package dev.metinkale.prayertimes.core


val Resources = nodejs.require("Resources/resources.js")

actual fun readFileAsLineSequence(filePath: String): Sequence<String> {
    var res: dynamic = Resources
    filePath.trim('/').split("/").forEach {
        res = res[it]
    }

    val lines: Array<String> = res as Array<String>
    return lines.asSequence()
}



/*
val resourcesPath = js("process").env.RESOURCES_PATH

actual fun readFileAsLineSequence(filePath: String): Sequence<String> {
    val string = fs.readFileSync(resourcesPath + "/" + filePath, "utf8") as String
    return string.lineSequence()
}*/