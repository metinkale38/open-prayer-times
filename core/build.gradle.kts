plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("maven-publish")
}


repositories {
    mavenCentral()
}



kotlin {
    jvm{
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js { nodejs { } }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlinx.coroutines.DelicateCoroutinesApi")
        }

        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                api(project(":praytimes"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(npm("n-readlines", "1.0.1"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.junit.jupiter:junit-jupiter-params:5.1.0")
            }
        }


        val jvmMain by getting {
            dependencies {
                implementation("com.google.maps:google-maps-services:2.1.0")
                implementation("org.slf4j:slf4j-simple:1.7.25")
            }
        }

    }
}
