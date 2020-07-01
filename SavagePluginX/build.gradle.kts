import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.jvm.tasks.Jar


// USE THIS AS A SUBMODULE -- VERSIONS AREN'T INCLUDED FOR PLUGINS ON PURPOSE.

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "net.savagelabs"
version = "v1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://nexus.savagelabs.net/repository/maven-releases/")
}



dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.11.1") {
        exclude("org.yaml", "snakeyaml")
    }
    implementation("me.rayzr522:jsonmessage:1.2.0")



    compileOnly("org.spigotmc:spigot-api:1.16.1-R0.1-SNAPSHOT")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }


    val build by existing {
        dependsOn(shadowJar)
    }


    val shadowJar = named<ShadowJar>("shadowJar") {
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
        archiveBaseName.set("SavagePluginX")
    }
}