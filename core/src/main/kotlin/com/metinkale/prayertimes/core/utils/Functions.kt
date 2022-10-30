package com.metinkale.prayertimes.core.utils

import kotlinx.datetime.*

fun LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
fun LocalDate.Companion.now() = LocalDateTime.now().date
fun LocalTime.Companion.now() = LocalDateTime.now().time

fun LocalDate.toDMY() = toString().split("-").reversed().joinToString(".")