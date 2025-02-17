package dev.metinkale.prayertimes.providers.sources.features

import dev.metinkale.prayertimes.providers.DayTimes
import dev.metinkale.prayertimes.providers.utils.now
import kotlinx.datetime.LocalDate

interface DayTimesFeature {
    suspend fun getDayTimes(key: String): List<DayTimes>
    suspend fun getDayTime(key: String, day: LocalDate = LocalDate.now()): DayTimes? =
        getDayTimes(key).firstOrNull { it.date == day }
}