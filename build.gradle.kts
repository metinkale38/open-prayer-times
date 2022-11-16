plugins {
    kotlin("multiplatform") version "1.7.20" apply false
    kotlin("plugin.serialization") version "1.7.20" apply false
}

allprojects {
    //manage common setting and dependencies
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "dev.metinkale.prayertimes"
    version = "1.0-SNAPSHOT"
}

tasks.findByName("appengineDeploy")?.dependsOn(tasks.findByName("shadowJar"))