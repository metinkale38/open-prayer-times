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
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
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
    }
}