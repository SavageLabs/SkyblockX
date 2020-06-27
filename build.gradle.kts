import org.apache.tools.ant.filters.ReplaceTokens
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "net.savagelabs"
version = "v1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://nexus.savagelabs.net/repository/maven-releases/")
    maven("https://rayzr.dev/repo/")
    maven("https://libraries.minecraft.net")
    maven("http://repo.dmulloy2.net/nexus/repository/public/")
    maven("http://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public")
}

dependencies {
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    compile("org.ocpsoft.prettytime:prettytime:4.0.1.Final")
    compile("com.github.stefvanschie.inventoryframework:IF:0.5.18")
    compile("org.bstats:bstats-bukkit:1.7")
    compile("com.google.code.gson:gson:2.8.5")
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("net.prosavage:BasePlugin:1.7.4")
    compile("me.rayzr522:jsonmessage:1.2.0")
    compile("com.cryptomorin:XSeries:5.3.3")
    compile("io.papermc:paperlib:1.0.2")

    compileOnly("org.spigotmc:spigot-api:1.15.1-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.9.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }


    processResources {
        filter<ReplaceTokens>("tokens" to mapOf(
            "project.version" to project.version
        ))
    }

    val shadowJar = named<ShadowJar>("shadowJar") {
        mergeServiceFiles()
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
        relocate(
            "com.github.stefvanschie.inventoryframework",
            "net.savagelabs.skyblockx.shade.stefvanschie.inventoryframework"
        )
        relocate("com.google.gson", "net.savagelabs.skyblockx.shade.com.google.gson")
        relocate("net.prosavage.baseplugin", "net.savagelabs.skyblockx.shade.baseplugin")
        relocate("org.jetbrains.kotlin", "net.savagelabs.skyblockx.shade.kotlin")
        relocate("me.rayzr522.jsonmessage", "net.savagelabs.skyblockx.shade.jsonmessage")
        relocate("org.bstats", "net.savagelabs.skyblockx.shade.bstats")
        relocate("io.papermc.lib", "net.savagelabs.skyblockx.shade.paperlib")
        relocate("kotlin", "net.savagelabs.skyblockx.shade.kotlin")
        relocate("org.jetbrains.annotations", "net.savagelabs.skyblockx.shade.jetbrains-annotations")
        relocate("com.cryptomorin.xseries", "net.savagelabs.skyblockx.shade.xseries")
        relocate("fonts", "net.savagelabs.skyblockx.shade.fonts")
        archiveBaseName.set("SkyblockX.jar")
        minimize()
    }



}