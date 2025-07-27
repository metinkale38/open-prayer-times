package dev.metinkale.hijri

import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal fun LocalDate.previousOrSame(targetDay: DayOfWeek): LocalDate {
    val daysToSubtract = (this.dayOfWeek.isoDayNumber - targetDay.isoDayNumber + 7) % 7
    return this.minus(daysToSubtract.toLong(), DateTimeUnit.DAY)
}


fun LocalDate.Companion.now(): LocalDate =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date