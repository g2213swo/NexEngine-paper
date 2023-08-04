plugins {
    `java-library`
    `maven-publish`
}

project.ext.set("name", "NexEngine")
version = "1.0.0-SNAPSHOT"
group = "su.nexmedia"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io/")
}

dependencies {
    // server api
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    // the "api" module
    // make it a separate module to avoid circular dependencies
    api(project(":NexEngineAPI"))

    // code that requires 3rd plugin dependencies
    // we put it in a separate module to avoid dependency pollution
    api(project(":NexEngineExt"))
    implementation(project(":NexEngineExt"))

    // nms modules
    api(project(":NMS"))
    implementation(project(":NexEngineCompat_V1_18_R2", configuration = "reobf"))
    implementation(project(":NexEngineCompat_V1_19_R3", configuration = "reobf"))
    implementation(project(":NexEngineCompat_V1_20_R1", configuration = "reobf"))

    // libs to be shaded
    compileOnly("org.xerial", "sqlite-jdbc", "3.42.+")
}

// publish to local maven repo
publishing {
    publications {
        create<MavenPublication>("NexEngine") {
            groupId = "su.nexmedia"
            artifactId = "NexEngine"
            version = "1.0.0-SNAPSHOT"
            from(components["java"])
        }

    }
}









