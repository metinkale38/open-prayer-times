# open-prayer-times

open-prayer-times is the core library powering the Prayer Times Android App (https://github.com/metinkale38/prayer-times-android).
It provides a Kotlin Multiplatform (KMP) implementation for retrieving Islamic prayer times — from both online sources and local calculations.

The goal is to offer a modular, platform-independent solution that can be reused across Android, iOS, desktop, and server environments.

---

## Project Modules

### core — Main Library (Provider Aggregator)

This is the primary multiplatform module that brings together both:

- Web-based providers (e.g. Diyanet, IGMG, NamazVakti)
- Offline calculation via the calctimes module

All providers implement a shared Source interface that offers:

- City search (by query or lat/lng)
- Daily prayer time retrieval
- Unified access regardless of provider type

Included Providers:
- Calc – Uses the internal calctimes module (offline)
- Diyanet – Presidency of Religious Affairs, Turkey
- IGMG – Islamische Gemeinschaft Millî Görüş, Germany
- London – Unified Islamic Prayer Timetable (londonprayertimes.com)
- NVC – NamazVakti.com
- Semerkand – Semerkand Takvimi (semerkandtakvimi.com)

Want to add another provider? Open a pull request!

Example:

    val entry = Diyanet.search("Braunschweig").firstOrNull()
    val times = entry?.let { Diyanet.getDayTimes(it.id) }

---

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

### server — REST API (Ktor)

A Ktor-based server exposing the full functionality of openprayertimes via a REST interface.

Features:
- Search cities by name or coordinates
- Retrieve daily prayer times from any provider
- Host your own backend for mobile or web clients
