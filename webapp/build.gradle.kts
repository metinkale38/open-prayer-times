import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node") version "7.0.2"
}


buildscript {
    repositories {
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.github.node-gradle:gradle-node-plugin:7.0.2")
    }
}

apply(plugin = "com.github.node-gradle.node")

tasks.register<NpmTask>("npmBuild") {
    inputs.dir("src")
    inputs.dir("public")
    inputs.files("package.json", "tsconfig.json")
    outputs.dir("build")
    args.addAll("run", "build")
}
tasks.register<NpmTask>("npmTest") {
    args.addAll("test")
}
tasks.register<NpmTask>("npmStart") {
    args.addAll("start")
}
