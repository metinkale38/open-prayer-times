package dev.metinkale.prayertimes.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.withContext


actual fun readFileAsLineSequence(filePath: String): Sequence<String> =
    Entry::class.java.getResourceAsStream(filePath)!!.bufferedReader(Charsets.UTF_8).lineSequence()
