plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("maven-publish")
}


kotlin {
    applyDefaultHierarchyTemplate()
    if (project.findProperty("skipNative") != null)
        linuxX64("linux")
    jvm()
    js {
        browser()
        nodejs()
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }

        commonMain.dependencies {
            api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            api("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
        }

        jsMain.dependencies {
            implementation(npm("@js-joda/timezone", "2.3.0"))
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }
    }
}
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["kotlin"])
            artifactId = "praytimes"
            groupId = project.rootProject.group.toString()
            version = project.version.toString()
        }
    }
}