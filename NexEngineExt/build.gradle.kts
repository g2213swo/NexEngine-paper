plugins {
    id("su.nexmedia.project-conventions")
}

dependencies {
    compileOnly(project(":NexEngineAPI"))

    compileOnly(libs.server.paper)

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
