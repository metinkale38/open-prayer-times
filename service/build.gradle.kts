import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    mavenCentral()
}

plugins {
    id("kotlin")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.google.cloud.tools.appengine") version "2.4.2"
    id("maven-publish")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}


dependencies {

    implementation(project(":core"))
    implementation("io.ktor:ktor-server-core:2.1.3")
    implementation("io.ktor:ktor-server-netty:2.1.3")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.1.0")
}

configure<AppEngineAppYamlExtension> {
    stage {
        setArtifact("build/libs/${project.name}-${project.version}-all.jar")
    }
    deploy {
        version = "GCLOUD_CONFIG"
        projectId = "GCLOUD_CONFIG"
    }
}
tasks.withType<ShadowJar> {
    manifest {
        attributes(mapOf("Main-Class" to "dev.metinkale.prayertimes.service.MainKt"))
    }
}

tasks.findByName("appengineStage")?.dependsOn(tasks.findByName("shadowJar"))