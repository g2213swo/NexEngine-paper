/*
 * This file was generated by the Gradle 'init' task.
 */

rootProject.name = "NexEnginePlugin"
include(":NexEngine")
include(":NexEngineCompat_V1_19_R1")
include(":NexEngineCompat_V1_19_R2")
include(":NexEngineCompat_V1_17_R1")
include(":NexEngineCompat_V1_18_R2")
include(":NMS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}