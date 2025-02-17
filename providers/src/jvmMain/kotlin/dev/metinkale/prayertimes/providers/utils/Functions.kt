package dev.metinkale.prayertimes.providers.utils

import kotlinx.datetime.*
import kotlin.math.roundToInt

internal fun LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
internal fun LocalDate.Companion.now() = LocalDateTime.now().date
internal fun LocalTime.Companion.now() = LocalDateTime.now().time

internal fun LocalDate.toDMY() = toString().split("-").reversed().joinToString(".")


/**
 * Rounds a Double to 3 decimal places, used for lat/lng
 *
 * 1/60 = 0.016 change in Location refers to one minute of change in prayer times
 *
 * by rounding to 3 decimal places, we have a maximum distance of 0.001, which is only a few seconds, so its negligible
 */
internal fun Double.roundLatLng(): Double = (this * 1000).roundToInt() / 1000.0