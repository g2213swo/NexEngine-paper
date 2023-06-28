plugins {
    id("su.nexmedia.project-conventions")
}

dependencies {
    compileOnly(libs.server.paper)
    compileOnlyApi(libs.mewcore)
}
