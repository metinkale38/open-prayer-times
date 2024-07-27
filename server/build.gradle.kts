val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
}


application {
    mainClass.set("dev.metinkale.prayertimes.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":hijri"))
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("com.ucasoft.ktor:ktor-simple-cache-jvm:0.4.3")
    implementation("com.ucasoft.ktor:ktor-simple-memory-cache-jvm:0.4.3")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}



tasks.named("compileJava") {
    dependsOn(":webapp:npmBuild")
}

tasks.register<Copy>("copyWebApp") {
    dependsOn(":webapp:npmBuild")
    from("$rootDir/webapp/build")
    into("$buildDir/resources/main/static/.")
}

tasks.named("processResources") {
    dependsOn("copyWebApp")
}