plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
    kotlin("jvm") version "2.4.10"
    id("io.ktor.plugin") version "3.5.2"
    application
}

application {
    mainClass.set("dev.metinkale.openprayertimes.server.ApplicationKt")
}


dependencies {
    implementation(project(":client"))
    implementation(project(":diyanet-client"))
    implementation(project(":hijri"))
    implementation(project(":calctimes"))
    implementation("io.ktor:ktor-server-core:3.5.2")
    implementation("io.ktor:ktor-server-cors:3.5.2")
    implementation("io.ktor:ktor-server-cio:3.5.2")
    implementation("io.ktor:ktor-server-content-negotiation:3.5.2")
    implementation("io.ktor:ktor-client-cio:3.5.2")
    implementation("io.ktor:ktor-client-core:3.5.2")
    implementation("io.ktor:ktor-client-logging:3.5.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.5.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    implementation("ch.qos.logback:logback-classic:1.6.3")


    "1.3.1".let { version ->
        implementation("org.jetbrains.exposed:exposed-core:${version}")
        implementation("org.jetbrains.exposed:exposed-jdbc:${version}")
        implementation("org.jetbrains.exposed:exposed-json:${version}")
        implementation("org.jetbrains.exposed:exposed-java-time:${version}")
    }

    implementation("org.postgresql:postgresql:42.7.2")
    implementation("com.zaxxer:HikariCP:5.1.0")
}



