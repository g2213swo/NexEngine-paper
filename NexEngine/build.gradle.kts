plugins {
    id("su.nexmedia.project-conventions")
    id("cc.mewcraft.publishing-conventions")
    id("cc.mewcraft.deploy-conventions")
    id("cc.mewcraft.paper-plugins")
}

project.ext.set("name", "NexEngine")

dependencies {
    // server api
    compileOnly(libs.server.paper)

    // the "api" module
    // make it a separate module to avoid circular dependencies
    api(project(":NexEngineAPI"))

    // code that requires 3rd plugin dependencies
    // we put it in a separate module to avoid dependency pollution
    api(project(":NexEngineExt"))

    // nms modules
    api(project(":NMS"))
    implementation(project(":NexEngineCompat_V1_18_R2", configuration = "reobf"))
    implementation(project(":NexEngineCompat_V1_19_R3", configuration = "reobf"))

    // libs to be shaded
    compileOnly("io.netty:netty-all:4.1.86.Final")
    compileOnly("org.xerial:sqlite-jdbc:3.40.0.0")
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
