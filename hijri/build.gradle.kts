plugins {
    kotlin("multiplatform")
    id("maven-publish")
}


kotlin {
    applyDefaultHierarchyTemplate()
    if (project.findProperty("skipNative") != null)
        linuxX64("linux") {}
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
            groupId = project.rootProject.group.toString()
            version = project.version.toString()
        }
    }
}