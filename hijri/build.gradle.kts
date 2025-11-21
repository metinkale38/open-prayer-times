plugins {
    kotlin("multiplatform")
    id("maven-publish")
}


kotlin {
    applyDefaultHierarchyTemplate()
    jvm()

    sourceSets {

        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }

        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
        }

        jvmMain.dependencies {
            implementation(kotlin("stdlib"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["kotlin"])
            artifactId = "hijri"
            groupId = project.group.toString()
            version = project.version.toString()
        }
    }
}