@file:Suppress("SpellCheckingInspection")

rootProject.name = "cloak"

pluginManagement {
  repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    google()
    mavenCentral()
    mavenLocal()
    gradlePluginPortal()
  }
}

plugins { id("com.meowool.gradle.toolkit") version "0.1.1-SNAPSHOT" }
////                                   # available:"0.2.2-LOCAL-SNAPSHOT"

buildscript {
  configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
  }
}

dependencyMapper {
  plugins {
    map("com.meowool.sweekt")
  }
}

gradleToolkitWithMeowoolSpec()

importProjects(rootDir)

// Only set in the CI environment, waiting the issue to be fixed:
// https://youtrack.jetbrains.com/issue/KT-48291
if (isCiEnvironment) extra["kotlin.mpp.enableGranularSourceSetsMetadata"] = true