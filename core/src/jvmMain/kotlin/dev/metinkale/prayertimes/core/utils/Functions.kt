package dev.metinkale.prayertimes.core.utils

import kotlinx.datetime.*
import kotlin.math.roundToInt

fun LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
fun LocalDate.Companion.now() = LocalDateTime.now().date
fun LocalTime.Companion.now() = LocalDateTime.now().time

fun LocalDate.toDMY() = toString().split("-").reversed().joinToString(".")


/**
 * Rounds a Double to 3 decimal places, used for lat/lng
 *
 * 1/60 = 0.016 change in Location refers to one minute of change in prayer times
 *
 * by rounding to 3 decimal places, we have a maximum distance of 0.001, which is only a few seconds, so its negligible
 */
fun Double.roundLatLng(): Double = (this * 1000).roundToInt() / 1000.0