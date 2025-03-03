plugins {
    kotlin("multiplatform") version "2.0.0" apply false
    kotlin("plugin.serialization") version "2.0.0" apply false
    kotlin("jvm") version "2.0.0" apply false
    id("io.ktor.plugin") version "2.3.12" apply false
    id("app.cash.sqldelight") version "2.0.2" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "dev.metinkale.prayertimes"
    version = "1.0-SNAPSHOT"
}