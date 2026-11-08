package dev.metinkale.openprayertimes.hijri

import kotlinx.datetime.LocalDate
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HijriConfigTest {
    @BeforeTest
    fun resetBefore() {
        HijriConfig.current = HijriConfiguration()
    }

    @AfterTest
    fun resetAfter() {
        HijriConfig.current = HijriConfiguration()
    }

    @Test
    fun defaultsToDiyanetWithoutOffset() {
        assertEquals(HijriConfiguration(), HijriConfig.current)

        val date = LocalDate(2022, 11, 16)
        assertEquals(HijriDate.of(1444, 4, 21), date.toHijriDate())
        assertEquals(date, HijriDate.of(1444, 4, 21).toLocalDate())
    }

    @Test
    fun switchesToUmmulQura() {
        HijriConfig.current = HijriConfiguration(HijriCalculation.UmmulQura)

        val date = LocalDate(2022, 11, 16)
        assertEquals(HijriDate.of(1444, 4, 22), date.toHijriDate())
        assertEquals(date, HijriDate.of(1444, 4, 22).toLocalDate())
    }

    @Test
    fun appliesPositiveDayOffset() {
        HijriConfig.current = HijriConfiguration(HijriCalculation.Diyanet, 1)

        val date = LocalDate(2022, 11, 16)
        assertEquals(HijriDate.of(1444, 4, 22), date.toHijriDate())
        assertEquals(date, HijriDate.of(1444, 4, 22).toLocalDate())
    }

    @Test
    fun appliesNegativeDayOffset() {
        HijriConfig.current = HijriConfiguration(HijriCalculation.Diyanet, -1)

        val date = LocalDate(2022, 11, 16)
        assertEquals(HijriDate.of(1444, 4, 20), date.toHijriDate())
    }

    @Test
    fun combinesCalculationAndOffset() {
        HijriConfig.current = HijriConfiguration(HijriCalculation.UmmulQura, 1)

        val date = LocalDate(2022, 11, 16)
        assertEquals(HijriDate.of(1444, 4, 23), date.toHijriDate())
        assertEquals(date, HijriDate.of(1444, 4, 23).toLocalDate())
    }

    @Test
    fun eventsFollowGlobalConfiguration() {
        HijriConfig.current = HijriConfiguration(HijriCalculation.UmmulQura)

        val ramadan = HijriDate.getEventsForHijriYear(1445)
            .first { it.second == HijriEvent.RAMADAN_BEGIN }
        assertEquals(HijriDate.of(1445, 9, 1), ramadan.first)
        assertEquals(LocalDate(2024, 3, 11), ramadan.first.toLocalDate())

        HijriConfig.current = HijriConfiguration(HijriCalculation.UmmulQura, 1)

        assertEquals(LocalDate(2024, 3, 10), ramadan.first.toLocalDate())
        val events = HijriDate.getEventsForGregYear(2024).toList()
        assertTrue(events.any { it.first == ramadan.first && it.second == HijriEvent.RAMADAN_BEGIN })
    }

    @Test
    fun eventsRoundTripWithOffset() {
        HijriConfig.current = HijriConfiguration(HijriCalculation.UmmulQura, 2)

        HijriDate.getEventsForHijriYear(1445).forEach { (date, _) ->
            assertEquals(date, date.toLocalDate().toHijriDate())
        }
    }
}
