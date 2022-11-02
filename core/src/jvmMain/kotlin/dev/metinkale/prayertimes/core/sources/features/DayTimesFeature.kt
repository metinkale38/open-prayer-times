package dev.metinkale.prayertimes.core.sources.features

import dev.metinkale.prayertimes.core.DayTimes
import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.utils.now
import kotlinx.datetime.LocalDate

interface DayTimesFeature : Source {
    suspend fun getDayTimes(key: String): List<DayTimes>
    suspend fun getDayTime(key: String, day: LocalDate = LocalDate.now()): DayTimes? =
        getDayTimes(key).firstOrNull { it.date == day }
}