plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
    kotlin("jvm") version "2.2.0"
    application
}

group = "dev.metinkale.diyanet.client"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-client-cio:2.3.7")
    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-auth:2.3.7")
    implementation("io.ktor:ktor-client-logging:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}