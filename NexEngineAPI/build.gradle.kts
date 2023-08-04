plugins {
    `java-library`
    `maven-publish`
}

project.ext.set("name", "NexEngineAPI")
version = "1.0.0-SNAPSHOT"
group = "su.nexmedia"

repositories{
    mavenCentral()
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://jitpack.io/")
}


dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    // libs embedded in core
    compileOnlyApi("com.zaxxer:HikariCP:5.0.1")
    compileOnlyApi("com.mojang:authlib:1.5.25")

}

publishing {
    publications {

        create<MavenPublication>("NexEngineAPI") {
            groupId = "su.nexmedia"
            artifactId = "NexEngineAPI"
            version = "1.0.0-SNAPSHOT"
            from(components["java"])
        }
    }
}