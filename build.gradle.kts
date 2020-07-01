import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    kotlin("jvm") version "1.3.61"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "net.savagelabs"
version = "v1.5"

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
    implementation("org.ocpsoft.prettytime:prettytime:4.0.1.Final")
    implementation("com.github.stefvanschie.inventoryframework:IF:0.5.18")
    implementation("org.bstats:bstats-bukkit:1.7")
    implementation("com.cryptomorin:XSeries:6.0.0")
    implementation("io.papermc:paperlib:1.0.2")
    implementation(project(":WorldBorderUtil"))
    implementation(project(":SavagePluginX"))


    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly(kotlin("reflect"))
    compileOnly("me.rayzr522:jsonmessage:1.2.0")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.0")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.11.0")
    compileOnly("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.11.1")
    compileOnly("org.spigotmc:spigot-api:1.16.1-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.9.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    processResources {
        filter<ReplaceTokens>(
            "tokens" to mapOf(
                "project.version" to project.version
            )
        )
    }

    val build by existing {
        dependsOn(shadowJar)
    }

    val copyResources by registering(Copy::class) {
        from("src/main/resources")
        into(buildDir.resolve("resources/main"))
        dependsOn(processResources)
    }

    val shadowJar = named<ShadowJar>("shadowJar") {
        dependsOn(copyResources)
        mergeServiceFiles()
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
        relocate("org.bstats", "net.savagelabs.skyblockx.shade.bstats")
        archiveFileName.set("SkyblockX-${version}.jar")
    }


    val ci = task("ci") {
        dependsOn(clean)
        dependsOn(shadowJar)
    }
}