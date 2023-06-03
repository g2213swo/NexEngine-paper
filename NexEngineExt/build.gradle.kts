plugins {
    id("su.nexmedia.project-conventions")
    id("cc.mewcraft.publishing-conventions")
}

description = "NexEngineExt"

dependencies {
    compileOnly(project(":NexEngineAPI"))

    compileOnly(libs.server.paper)

    // My own library
    compileOnlyApi(libs.mewcore)

    // 3rd party plugins that may contain random transitive dependencies
    compileOnly(libs.papi)
    compileOnly(libs.vault) { isTransitive = false }
    compileOnly(libs.worldguard) {
        exclude("org.bukkit")
    }
    compileOnly(libs.citizens) {
        exclude("ch.ethz.globis.phtree")
    }
    compileOnly(libs.floodgate)
    compileOnly(libs.mythicmobs) { isTransitive = false }
}
