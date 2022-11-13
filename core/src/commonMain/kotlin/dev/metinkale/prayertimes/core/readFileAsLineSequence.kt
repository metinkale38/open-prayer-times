package dev.metinkale.prayertimes.core

import kotlinx.coroutines.channels.ReceiveChannel

expect fun readFileAsLineSequence(filePath: String): Sequence<String>
