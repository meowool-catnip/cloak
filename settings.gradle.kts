/*
 * Copyright (c) 2022. The Meowool Organization Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
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
// //                                   # available:"0.2.2-LOCAL-SNAPSHOT"

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
