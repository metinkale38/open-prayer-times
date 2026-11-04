plugins {
    kotlin("jvm")
    id("maven-publish")
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    testImplementation(kotlin("test"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlin.time.ExperimentalTime"
        )
    }
}

tasks.withType<Jar>().configureEach {
    from("LICENSE") { into("META-INF") }
    from("LICENSE.GPL-3.0") { into("META-INF") }
    from("NOTICE.txt") { into("META-INF") }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "calctimes"
            pom {
                licenses {
                    license {
                        name.set("GNU Lesser General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/lgpl-3.0.html")
                        distribution.set("repo")
                    }
                }
            }
        }
    }
}