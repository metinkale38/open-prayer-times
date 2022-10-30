package com.metinkale.prayertimes.core

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class DayTimes(
    val date: LocalDate,
    val fajr: LocalTime,
    val sun: LocalTime,
    val dhuhr: LocalTime,
    val asr: LocalTime,
    val maghrib: LocalTime,
    val ishaa: LocalTime,
    val asrHanafi: LocalTime? = null,
    val sabah: LocalTime? = null,
)