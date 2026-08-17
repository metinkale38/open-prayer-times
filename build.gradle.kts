plugins {
    kotlin("multiplatform") version "2.4.10" apply false
    kotlin("plugin.serialization") version "2.4.10" apply true
    kotlin("jvm") version "2.4.10" apply false
    id("io.ktor.plugin") version "3.5.2" apply false
    id("maven-publish")
}

allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
}

subprojects {
    group = "dev.metinkale"
    version = "1.0-SNAPSHOT"
}
