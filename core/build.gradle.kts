plugins{
    id("kotlin")
    kotlin("plugin.serialization") version "1.6.20"
}




repositories {
    mavenCentral()
}

dependencies{
    api(kotlin("stdlib"))
    api("org.http4k:http4k-core:4.32.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("io.ktor:ktor-client-okhttp:2.1.2")
}