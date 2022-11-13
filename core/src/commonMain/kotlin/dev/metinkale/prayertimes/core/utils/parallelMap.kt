package dev.metinkale.prayertimes.core.utils

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

suspend fun <F, T> List<F>.parallelMap(block: suspend (F) -> T) = map {
    GlobalScope.async { block.invoke(it) }
}.map { it.await() }