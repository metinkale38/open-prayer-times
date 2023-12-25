plugins {
    kotlin("multiplatform") version "1.8.20" apply false
    kotlin("plugin.serialization") version "1.8.20" apply false
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

tasks.findByName("appengineDeploy")?.dependsOn(tasks.findByName("shadowJar"))