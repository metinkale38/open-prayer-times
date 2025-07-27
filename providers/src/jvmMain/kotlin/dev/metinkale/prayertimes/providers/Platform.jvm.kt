package dev.metinkale.prayertimes.providers

import java.io.FileNotFoundException

internal actual val Platform: IPlatform = object : IPlatform {
    override fun getEnv(name: String): String? = System.getenv(name)?.takeIf { it.isNotEmpty() }

    override fun readFile(path: String): Sequence<String> {
        return Platform::class.java.getResourceAsStream(path)
            ?.bufferedReader()?.run {
                sequence {
                    useLines {
                        it.forEach { line -> yield(line) }
                    }
                }
            } ?: throw FileNotFoundException(path)
    }
}