# open-prayer-times

As the developer of an android app for prayer times (see https://github.com/metinkale38/prayer-times-android) i wanted to extract the prayer-times modules into a plattform independent library project and a very basic HTTP-Server, so it can be also used by other projects.

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

## core

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

#### Secrets.kt

The code in version control is missing a file called `Secrets.kt`. It contains some private keys used by the core. To compile this project, you have to add a Secrets.kt file.

    package dev.metinkale.prayertimes.core

    object Secrets {
        val googleApiKey = "GOOGLE_API_KEY" // needed for Geocoding
        val igmgApiKey = "IGMG_API_KEY" // needed to access igmg times
        val londonPrayerTimesKey = "LONDON_PRAYER_TIMES" // needed to access London Prayer Times
    }

### router

This module also contains a minimal http-routing interface which was used to implement a `coreRouter` to handle HTTP-Requests. It has no dependencies and must be wrapped with a HTTP-Server to be usable. 

Why do i need a router for a library project?

I wanted to keep the main parts independent of plattform code, so it can be used everywhere. For example you could easily deploy the core as router which Spring or other Backend-Frameworks and write small wrappers around the `coreRouter`. 

At first tried to deploy this library as cloud function in the google cloud. I tried the JVM-Engine which had very bad cold start times (which are crucial for cloud functions). Then i tried to compile the kotlin multiplattform project into a node.js server, which gave me much better cold start times, but searching for cities still took too long, so i decided to deploy the server as a Google App Engine Server. The server code resides in the `server` Module


## server

A simple wrapper around the `coreRouter` (see above) to deploy it in Googles App Engine plattform.