import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

repositories {
    mavenCentral()
}

plugins{
    id("kotlin")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("plugin.serialization") version "1.6.20"
}

val invoker by configurations.creating


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

configurations {
    invoker
}


dependencies{
    api(kotlin("stdlib"))
    implementation(project(":core"))

    implementation("org.http4k:http4k-serverless-gcf:4.33.1.0")

    // Every function needs this dependency to get the Functions Framework API.
    compileOnly("com.google.cloud.functions:functions-framework-api:1.0.4")

    // To run function locally using Functions Framework's local invoker
    invoker("com.google.cloud.functions.invoker:java-function-invoker:1.1.1")
}

tasks.withType<ShadowJar> {
    archiveFileName.set("shadowJar/function.jar")
    mergeServiceFiles()
    manifest {
        attributes(mapOf("Main-Class" to "com.metinkale.prayertimes.cloudfunctions.App"))
    }
}

task<JavaExec>("runFunction") {
    main = "com.google.cloud.functions.invoker.runner.Invoker"
    classpath(invoker)
    inputs.files(configurations.runtimeClasspath, sourceSets["main"].output)
    args(
        "--target", project.findProperty("runFunction.target") ?: "com.metinkale.prayertimes.cloudfunctions.App",
        "--port", project.findProperty("runFunction.port") ?: 8080
    )
    doFirst {
        args("--classpath", files(configurations.runtimeClasspath, sourceSets["main"].output).asPath)
    }
}
