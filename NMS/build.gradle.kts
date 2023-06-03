plugins {
    id("su.nexmedia.project-conventions")
    id("cc.mewcraft.publishing-conventions")
}

description = "NMS"

dependencies {
    compileOnly(libs.server.paper)
    compileOnly("io.netty:netty-all:4.1.85.Final")
}
