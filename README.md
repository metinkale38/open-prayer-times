# open-prayer-times

open-prayer-times is the core library powering the Prayer Times Android App
(https://github.com/metinkale38/prayer-times-android). It provides a Kotlin/JVM
implementation for retrieving Islamic prayer times, from both online sources and
local calculations.

The project is split into Gradle modules so the calculation, client, and server
logic can be reused independently. The current build targets the JVM; the module
boundaries are intended to allow additional targets later, but Kotlin
Multiplatform is not enabled yet.

## Project Modules

### calctimes - Local Calculation Engine

Offline prayer time calculations based on a Kotlin port of
[PrayTimes.org](http://praytimes.org/).

- No internet connection required
- Multiple calculation methods: MWL, ISNA, Egypt, Makkah, Karachi, UOIF,
  Russia, Singapore, Tehran, Jafari
- Configurable high-latitude adjustments and midnight modes
- Uses latitude, longitude, elevation, timezone, and date

```kotlin
import dev.metinkale.calctimes.CalcTimes
import dev.metinkale.calctimes.Method
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

val calc = CalcTimes(
    52.52,
    13.405,
    34.0,
    TimeZone.of("Europe/Berlin"),
    Method.MWL
)
val times = calc.getPrayerTimes(LocalDate(2022, 11, 16))

val fajr = times.fajr
val sunrise = times.sunrise
val dhuhr = times.dhuhr
val asr = times.asrShafi
val maghrib = times.maghrib
val ishaa = times.ishaa
```

#### Provenance and License

`calctimes` is a Kotlin port of **PrayTimes.js 2.3**. It was translated from
the author's earlier Java port of the same version. The `PrayTimes-Java 0.9`
project is not the source; it reused code from that earlier port.

The upstream library carries this header:

```text
PrayTimes.js: Prayer Times Calculator (ver 2.3)
Copyright (C) 2007-2011 PrayTimes.org
Developer: Hamid Zarrabi-Zadeh
License: GNU LGPL v3.0
```

History in this repository:

- Added on 2022-11-02 in commit `dd424a4` as the `praytimes` module.
- Renamed to `calctimes` in commit `271c118`.
- The older 2015 `vakit/times/PrayTime.java` in `prayer-times-android` is a
  separate PrayTimes 1.0 port and is not the source of this module.

PrayTimes.js 2.3 and its Java port are licensed under **GNU LGPL v3.0**. As a
derivative work, `calctimes` must be treated as LGPL-3.0. Redistributions must
retain the copyright notice, credit PrayTimes.org, link back to the original
project, and comply with the LGPL (including source availability for
modifications). It cannot be relicensed as MIT or Apache-2.0 only.

The repository currently has no top-level `LICENSE` file. The one-line
`calctimes/NOTICE.txt` is not a replacement for the full LGPL-3.0 text.

#### Differences from PrayTimes.js 2.3

The astronomy core is based on 2.3: Julian day conversion, solar declination,
equation of time, sunrise/sunset angle, Asr shadow factors, high-latitude night
portions, and midnight calculation. The public API and several behaviors differ:

- Kotlin/JVM API using `kotlinx-datetime`. The caller supplies a `TimeZone`, and
  `CalcTimes.getPrayerTimes(LocalDate)` returns typed `LocalTime` values instead
  of the JavaScript string-based `getTimes()` result.
- Returns `imsak`, `fajr`, `sunrise`, `zawal`, `dhuhr`, `asrShafi`,
  `asrHanafi`, `sunset`, `maghrib`, `ishaa`, `firstThird`, `midnight`, and
  `lastThird`. The original returns only one Asr value and no Zawal or night
  thirds.
- Adds `HighLatsType.Max` and `HighLatsType.Strict`. `Max` matches the original
  behavior of clamping whenever a calculated time exceeds the night portion;
  `Strict` only adjusts when the time cannot be calculated.
- Adds `HighLatsAdjustment.OneThird` and `HighLatsAdjustment.OneFifth`. The
  original 2.3 modes are `None`, `NightMiddle`, `OneSeventh`, and `AngleBased`.
- Adds method presets `UOIF`, `Russia`, and `Singapore`. `UOIF` was added to the
  Java port later; `Russia` and `Singapore` were added in this repository.
- Exposes per-prayer minute offsets and angle overrides through `Method`, plus a
  `useElevation` switch. The original always applies the elevation correction.
- Defaults differ. The bundled presets use `HighLatsAdjustment.None` and set
  Imsak to the Fajr angle. PrayTimes.js 2.3 defaults to `NightMiddle` and
  calculates Imsak 10 minutes before Fajr. Set the method fields explicitly if
  you need behavior identical to the original.
- The JavaScript `adjust()`, `tune()`, `getMethod()`, `getSetting()`, and
  string-formatting APIs are replaced by immutable `Method` values and
  `Method.copy(...)`.

### client - Hosted API Client

JVM client for the hosted open-prayer-times API at `https://opt.mk38.de`.

```kotlin
import dev.metinkale.openprayertimes.OpenPrayerTimes

val cities = OpenPrayerTimes.search("Berlin")
val times = OpenPrayerTimes.fetch(cities.first().provider, cities.first().id)
```

### server - REST API (Ktor)

Ktor server exposing cities, prayer times, Hijri dates, and events. Prayer times
from external providers are cached in PostgreSQL.

Supported providers:

- Diyanet - Presidency of Religious Affairs, Turkey
- IGMG - Islamische Gemeinschaft Milli Gorus, Germany
- London - Unified Islamic Prayer Timetable (londonprayertimes.com)
- NVC - NamazVakti.com
- Semerkand - Semerkand Takvimi (semerkandtakvimi.com)
- Calc - Local calculation from GeoNames coordinates using `calctimes`

### hijri - Hijri Date Conversion

Converts between Gregorian and Hijri dates using Diyanet mappings and provides
Hijri month events.

```kotlin
import dev.metinkale.openprayertimes.hijri.toHijriDate
import kotlinx.datetime.LocalDate

val hijri = LocalDate(2022, 11, 16).toHijriDate()
```

### diyanet-client - Diyanet API Client

Low-level client for the Diyanet Awqat Salah API. It handles login, JWT access
tokens, token refresh, place lookup, and daily/monthly/yearly prayer times. It is
used by the server and is not required for local calculations.

## Requirements

- JDK 17 or newer (the Gradle wrapper is included)
- PostgreSQL for the server
- Network access for external provider requests and dependency resolution

## Build and Test

```bash
./gradlew build
./gradlew test
```

Individual modules can be built or tested with their Gradle path, for example:

```bash
./gradlew :calctimes:test
./gradlew :server:build
```

## Run the Server

The server reads its database configuration from environment variables:

```bash
export JDBC_URL="jdbc:postgresql://localhost:5432/openprayertimes"
export JDBC_USERNAME="openprayertimes"
export JDBC_PASSWORD="secret"
export DB_POOL_SIZE="5" # optional

./gradlew :server:run
```

It listens on `0.0.0.0:8080`. The server expects an existing PostgreSQL schema
with the provider/city tables, the `times` cache table, the `geonames` table, and
the `join_names` database function. Schema migrations are not included in this
repository.

## REST API

Common routes:

```text
GET /search?q=Berlin
GET /search?lat=52.52&lng=13.405
GET /                                      # list providers
GET /{provider}/...                        # list cities or resolve a city
GET /{provider}/{city}/today
GET /{provider}/{city}/json                # remaining days of the current year
GET /{provider}/{city}/csv
GET /{provider}/{city}/{yyyy-mm-dd}
GET /date
GET /date/{yyyy-mm-dd}
GET /date/{year}                           # Hijri events in a Gregorian year
GET /hijri/{yyyy-mm-dd}
GET /hijri/{year}                          # Hijri events in a Hijri year
```

`/api/...` is still available as a deprecated alias for the same routes.

## License

`calctimes` is subject to GNU LGPL v3.0 because it derives from PrayTimes.js 2.3.
See the provenance section above. A top-level `LICENSE` file and licensing
information for the other modules are currently missing from this repository.
