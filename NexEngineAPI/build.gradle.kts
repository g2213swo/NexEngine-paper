plugins {
    id("su.nexmedia.project-conventions")
    id("cc.mewcraft.publishing-conventions")
    alias(libs.plugins.indra)
}

description = "NexEngineAPI"

dependencies {
    compileOnly(libs.server.paper)
    compileOnlyApi(libs.mewcore)
}

indra {
    javaVersions().target(17)
}