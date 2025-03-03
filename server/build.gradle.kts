val kotlin_version: String by project
val logback_version: String by project
val ktor_version="3.1.0"

plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("app.cash.sqldelight")
}

sqldelight {
    databases {
        create("Times") {
            packageName.set("org.metinkale.prayertimes.db")
            srcDirs.setFrom("src/main/kotlin")
        }
    }
}




application {
    mainClass.set("dev.metinkale.prayertimes.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":providers"))
    implementation(project(":hijri"))
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("app.cash.sqldelight:sqlite-driver:2.0.2")
}
