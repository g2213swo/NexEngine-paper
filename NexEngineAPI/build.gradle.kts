plugins {
    id("su.nexmedia.project-conventions")
}

dependencies {
    compileOnly(libs.server.paper)

    // core libs
    compileOnlyApi(libs.mewcore)
    // libs embedded in core
    compileOnlyApi(libs.hikari)
    compileOnlyApi(libs.authlib)
}
