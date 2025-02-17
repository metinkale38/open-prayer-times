# open-prayer-times

As the developer of an android app for prayer times (see https://github.com/metinkale38/prayer-times-android) i wanted to extract the prayer-times modules into a plattform independent library project, so it can be also used by other projects.

## praytimes module

A multiplattform module for calculating prayer times for different calculation methods. Its a kotlin port from http://praytimes.org/, which was originally written in javascript. 

Usage:

    val pt = PrayTimes(52.26, -10.51, 0.0, TimeZone.UTC, Method.MWL)
    val times = pt.getTimes(LocalDate(2022, 11, 16))
    val fajr = times.fajr
    val sunrise = times.sunrise
    val dhuhr = times.dhuhr
    val asr = times.asrShafi 
    val maghrib = times.maghrib
    val ishaa = times.ishaa

This module does not need an active internet connection. 

## providers module

This is also a multi-plattform module, which contains some web-based prayer-time providers like Diyanet, IGMG, NamazVakti.com and SemerkandTakvimi (feel free to make PRs for more providers).

Each time-provider is a object implementing a `Source` Interface which provides features like searching cities (by query or lat/lng) and provides a easy way to get prayer times for a day.

Usage:

    val entry = Diyanet.search("Braunschweig").firstOrNull()
    val times = entry?.let{ Diyanet.getDayTimes(it.id) }

Sources (feel free to create pull requests with new sources)
 - Calc (based on praytimes module)
 - Diyanet (Presidency of Religious Affairs - Turkey)
 - IGMG (Islamische Gemeinschaft Millî Görüş - Germany)
 - London (Unified Islamic Prayer Timetable for London - londonprayertimes.com)
 - NVC (NamazVakti.com)
 - Semerkand (semerkandtakvimi.com)


