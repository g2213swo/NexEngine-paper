plugins {
    `java-library`
    `maven-publish`
}

project.ext.set("name", "NMS")
version = "1.0.0-SNAPSHOT"
group = "su.nexmedia"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
}
dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("NMS") {
            groupId = "su.nexmedia"
            artifactId = "NMS"
            version = "1.0.0-SNAPSHOT"
            from(components["java"])
        }
    }
}