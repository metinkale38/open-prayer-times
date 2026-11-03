
plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
    kotlin("jvm") version "2.4.10"
    application
}

group = "dev.metinkale.diyanet.client"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.auth0:java-jwt:4.6.0")
    implementation("io.ktor:ktor-client-cio:3.5.2")
    implementation("io.ktor:ktor-client-core:3.5.2")
    implementation("io.ktor:ktor-client-auth:3.5.2")
    implementation("io.ktor:ktor-client-logging:3.5.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.5.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
}

tasks.test {
    useJUnitPlatform()
}
