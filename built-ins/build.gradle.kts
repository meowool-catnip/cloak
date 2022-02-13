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
plugins {
//  id(Plugins.Meowool.Sweekt)
}

androidLib()

configurations.all {
  resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

jvmTarget()

androidTarget {
  main.dependsOn(jvmMainSourceSet)
  main.dependencies {
    compileOnlyOf(
      Libs.AndroidX.Appcompat,
      Libs.AndroidX.Core.Ktx,
      Libs.AndroidX.Activity.Ktx,
      Libs.AndroidX.Fragment.Ktx,
    )
  }
}

afterEvaluate {
  mapOf(
    jvmMainSourceSet to "jvm.src",
    androidMainSourceSet to "android.src",
  ).forEach { (mainSourceSet, file) ->
    // Save the path of the main source set
    ext.set(file, mainSourceSet.kotlin.srcDirs.first { it.exists() }.absolutePath)
  }
}
