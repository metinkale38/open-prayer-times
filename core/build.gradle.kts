plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}


repositories {
    mavenCentral()
}

kotlin {
    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                api(kotlin("stdlib"))
                api("org.http4k:http4k-core:4.32.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("io.ktor:ktor-client-core:2.1.2")
                implementation(project(":praytimes"))
                implementation("io.ktor:ktor-client-okhttp:2.1.2")
                implementation("com.google.maps:google-maps-services:2.1.0")
                implementation("org.slf4j:slf4j-simple:1.7.25")
            }
        }
    }
}
