plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}


kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                api(kotlin("reflect"))
            }
        }
        val commonTest by getting{
            dependencies{
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-junit"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}