plugins {
    id("su.nexmedia.project-conventions")
    id("cc.mewcraft.publishing-conventions")
    alias(libs.plugins.shadow)
    alias(libs.plugins.indra)
}

description = "NexEngine"

dependencies {
    api(project(":NexEngineAPI"))

    // Server API
    compileOnly(libs.server.paper)

    // NMS modules
    api(project(":NMS"))
    implementation(project(":NexEngineCompat_V1_18_R2", configuration = "reobf"))
    implementation(project(":NexEngineCompat_V1_19_R2", configuration = "reobf"))
    implementation(project(":NexEngineCompat_V1_19_R3", configuration = "reobf"))

    // Internal libraries
    compileOnly("io.netty:netty-all:4.1.86.Final")
    compileOnly("org.xerial:sqlite-jdbc:3.40.0.0")

    // 3rd party plugins
    api(project(":NexEngineExt"))
}

// TODO remove plugin.yml
/*bukkit {
    main = "su.nexmedia.engine.NexEngine"
    name = "NexEngine"
    version = "${project.version}"
    apiVersion = "1.17"
    authors = listOf("NightExpress")
    softDepend = listOf("Vault", "Citizens", "MythicMobs")
    load = STARTUP
    libraries = listOf("com.zaxxer:HikariCP:5.0.1", "it.unimi.dsi:fastutil:8.5.11")
}*/

tasks {
    build {
        dependsOn(shadowJar)
    }
    jar {
        archiveClassifier.set("noshade")
    }
    shadowJar {
        minimize {
            exclude(dependency("su.nexmedia:.*:.*"))
        }
        archiveFileName.set("NexEngine-${project.version}.jar")
        archiveClassifier.set("")
        destinationDirectory.set(file("$rootDir"))
    }
    processResources {
        filesMatching("**/paper-plugin.yml") {
            expand(mapOf(
                "version" to "${project.version}",
                "description" to project.description
            ))
        }
    }
    register("deployJar") {
        doLast {
            exec {
                commandLine("rsync", shadowJar.get().archiveFile.get().asFile.absoluteFile, "dev:data/dev/jar")
            }
        }
    }
    register("deployJarFresh") {
        dependsOn(build)
        finalizedBy(named("deployJar"))
    }
}

indra {
    javaVersions().target(17)
}