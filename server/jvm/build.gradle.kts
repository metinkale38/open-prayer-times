plugins {
    kotlin("jvm") version "2.2.0"
    id("io.ktor.plugin") version "3.2.2"
    application
}

application {
    mainClass.set("dev.metinkale.prayertimes.MainKt")
}


dependencies {
    implementation(project(":server"))
}

