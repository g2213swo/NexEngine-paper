plugins {
    `java-library`
    `maven-publish`
}

project.ext.set("name", "NexEngineExt")
version = "1.0.0-SNAPSHOT"
group = "su.nexmedia"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://mvn.lumine.io/repository/maven-public/")
    maven("https://jitpack.io/")
    maven { url = uri("https://maven.enginehub.org/repo/") }
    maven {
        url = uri("https://repo.opencollab.dev/main/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        url = uri("https://maven.citizensnpcs.co/repo")
    }
}

dependencies {
    compileOnly(project(":NexEngineAPI"))

    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    // 3rd party plugins that may contain random transitive dependencies
    compileOnly("me.clip:placeholderapi:2.11.3")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") { isTransitive = false }
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9") {
        exclude("org.bukkit")
    }

    compileOnly("net.citizensnpcs:citizens-main:2.0.30-SNAPSHOT") {
        exclude(group = "*", module = "*")
    }

    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT")
    compileOnly("io.lumine:Mythic-Dist:5.2.1") { isTransitive = false }
}
publishing {
    publications {

        create<MavenPublication>("NexEngineExt") {
            groupId = "su.nexmedia"
            artifactId = "NexEngineExt"
            version = "1.0.0-SNAPSHOT"
            from(components["java"])
        }
    }
}