plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}


kotlin {
    js {
        nodejs()
    }
    jvm()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                api("io.github.microutils:kotlin-logging:3.0.2")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm("@js-joda/timezone", "2.3.0"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-junit"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                api("org.slf4j:slf4j-simple:2.0.3")
            }
        }
    }
}