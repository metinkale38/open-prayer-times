package dev.metinkale.openprayertimes.server.utils

import kotlin.reflect.KClass

fun <T : Any> KClass<T>.sealedInstances(): List<T> =
    if (this.isSealed) {
        this.sealedSubclasses.flatMap { it.sealedInstances() }
    } else listOfNotNull(this.objectInstance)
