# open-prayer-times

open-prayer-times is the core library powering the Prayer Times Android App (https://github.com/metinkale38/prayer-times-android).
It provides a Kotlin Multiplatform (KMP) implementation for retrieving Islamic prayer times — from both online sources and local calculations.

The goal is to offer a modular, platform-independent solution that can be reused across Android, iOS, desktop, and server environments.

---

## Project Modules


### server — REST API (Ktor)

A Ktor-based server exposing the full functionality of openprayertimes via a REST interface.

Features:
- Search cities by name or coordinates
- Retrieve daily prayer times from any provider
- Prayer times are cached on a postgres server


Supported prayer times providers
- Diyanet – Presidency of Religious Affairs, Turkey
- IGMG – Islamische Gemeinschaft Millî Görüş, Germany
- London – Unified Islamic Prayer Timetable (londonprayertimes.com)
- NVC – NamazVakti.com
- Semerkand – Semerkand Takvimi (semerkandtakvimi.com)

Want to add another provider? Open a pull request!

### calctimes — Local Calculation Engine

This module provides offline prayer time calculations using a Kotlin port of praytimes.org (http://praytimes.org/).

Features:
- No internet connection required
- Supports multiple calculation methods:
    - MWL, ISNA, Umm al-Qura, Makkah, etc.
- Accurate results based on latitude, longitude, elevation, and timezone

Example:

    val pt = PrayTimes(52.26, -10.51, 0.0, TimeZone.UTC, Method.MWL)
    val times = pt.getTimes(LocalDate(2022, 11, 16))

    val fajr = times.fajr
    val sunrise = times.sunrise
    val dhuhr = times.dhuhr
    val asr = times.asrShafi
    val maghrib = times.maghrib
    val ishaa = times.ishaa

---

### hijri — Hijri Date Conversion

A lightweight library to convert between Gregorian and Hijri dates using official Diyanet mappings.

Features:
- Gregorian ↔ Hijri conversion
- Uses authoritative Diyanet calendar data

---

