plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("io.papermc.paperweight.userdev") version "1.3.3"
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

repositories {
    mavenCentral()
}

version = Project.VERSION
description = "See Channel's Twitch Chat in Minecraft"

tasks {
    jar {
        enabled = false
    }
    build {
        dependsOn(reobfJar, shadowJar)
    }
    processResources {
        filesMatching("**/plugin.yml") {
            expand("version" to project.version)
        }
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    runServer {
        minecraftVersion("1.18.1")
    }
}

dependencies {
    paperDevBundle(Project.MINECRAFT_VERSION)
    implementation(group = "com.github.twitch4j", name = "twitch4j", version = Project.TWITCH4J_VERSION)
    implementation(group = "net.axay", name = "kspigot", version = Project.KSPIGOT_VERSION)
    implementation(group = "com.mojang", name = "brigadier", version = Project.BRIGADIER_VERSION)
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
    }
}
