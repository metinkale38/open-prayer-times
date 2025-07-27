plugins {
    kotlin("multiplatform") version "2.2.0" apply false
    kotlin("plugin.serialization") version "2.2.0" apply true
    kotlin("jvm") version "2.2.0" apply false
    id("io.ktor.plugin") version "3.2.2" apply false
    id("app.cash.sqldelight") version "2.1.0" apply false
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

    plugins.withId("maven-publish") {

        publishing {
            repositories {
                maven {
                    name = "GHCR"
                    url = uri("https://maven.pkg.github.com/metinkale38/open-prayer-times")

                    credentials {
                        username = System.getenv("USERNAME")
                        password = System.getenv("TOKEN")
                    }
                }
            }
        }
    }
}
