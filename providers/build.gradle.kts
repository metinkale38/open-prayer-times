plugins {
    kotlin("plugin.serialization")
    kotlin("multiplatform")
    id("maven-publish")
}



kotlin {
    applyDefaultHierarchyTemplate()
    if (project.findProperty("skipNative") != null)
        linuxX64("linux")
    jvm()

    sourceSets {

        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlinx.coroutines.DelicateCoroutinesApi")
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }


        val ktor_version = "3.2.2"

        commonMain.dependencies {
            implementation("io.ktor:ktor-client-cio:$ktor_version")
            api("io.ktor:ktor-client-core:$ktor_version")
            api("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
            api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
            api(project(":praytimes"))
        }


        jvmTest.dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-common"))
            implementation(kotlin("test-junit"))
            implementation(kotlin("test-annotations-common"))
        }

    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["kotlin"])
            artifactId = "open-prayer-times"
            groupId = project.group.toString()
            version = project.version.toString()
        }
    }
}