val logback_version: String by project
val ktor_version: String by project
val sqldelight_version: String by project

plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
    id("app.cash.sqldelight")
    kotlin("jvm") version "2.2.0"
    id("io.ktor.plugin") version "3.2.2"
    application
}

sqldelight {
    databases {
        create("TimesDatabase") {
            packageName.set("dev.metinkale.openprayertimes.db")
            srcDirs.setFrom("src/main/kotlin")
        }
    }
}

application {
    mainClass.set("dev.metinkale.openprayertimes.ApplicationKt")
}


dependencies {
    implementation(project(":core"))
    implementation(project(":hijri"))
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("app.cash.sqldelight:runtime:$sqldelight_version")
    implementation("app.cash.sqldelight:sqlite-driver:${sqldelight_version}")
}



