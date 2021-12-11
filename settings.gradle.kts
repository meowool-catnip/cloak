@file:Suppress("SpellCheckingInspection")

rootProject.name = "baize"

pluginManagement {
  repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins {
  id("com.meowool.gradle.toolkit") version "0.1.0"
}

dependencyMapper {
  libraries {
    map("dev.zacsweers.autoservice:auto-service-ksp" to "Auto.Service.Ksp")
  }
}

gradleToolkitWithMeowoolSpec()

importProjects(rootDir)