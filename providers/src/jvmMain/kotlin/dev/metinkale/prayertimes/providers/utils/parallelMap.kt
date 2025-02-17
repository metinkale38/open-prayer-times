package dev.metinkale.prayertimes.providers.router

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

internal suspend fun <F, T> List<F>.parallelMap(block: suspend (F) -> T) =
    if (size == 1) this.map { block.invoke(it) }
    else map {
        GlobalScope.async { block.invoke(it) }
    }.map { it.await() }