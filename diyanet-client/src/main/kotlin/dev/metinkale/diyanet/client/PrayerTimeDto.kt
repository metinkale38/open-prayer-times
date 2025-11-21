package dev.metinkale.diyanet.client

import kotlinx.serialization.Serializable

@Serializable
data class PrayerTime(
    val imsak: String,
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val date: String
)