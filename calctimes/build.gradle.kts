plugins {
    kotlin("multiplatform")
    id("maven-publish")
}


kotlin {
    jvm()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }

        commonMain.dependencies {
            api("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
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
            artifactId = "calctimes"
            groupId = project.group.toString()
            version = project.version.toString()
        }
    }
}