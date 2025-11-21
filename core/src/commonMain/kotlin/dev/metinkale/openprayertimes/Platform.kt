package dev.metinkale.openprayertimes

internal interface IPlatform {
    fun getEnv(name: String): String?

    fun readFile(path: String): Sequence<String>
}

internal expect val Platform : IPlatform