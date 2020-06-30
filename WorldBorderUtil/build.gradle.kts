plugins {
    java
}

group = "net.savagelabs"
version = "v1.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.1-R0.1-SNAPSHOT")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}