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
plugins { id(Plugins.Android.Junit5) }

androidKotlinLib {
  sourceSets {
    main {
      java.srcDir("sources")
      resources.srcDir("resources")
      manifest.srcFile("AndroidManifest.xml")
    }
    arrayOf("jvm.src", "android.src").forEach {
      // Get and save the paths of all parent project main source sets to the resource directory
      file("resources").resolve(it).writeText(parent!!.ext[it].toString())
    }
  }
}

dependencies {
  implementationOf(
    Libs.AndroidX.Appcompat,
    Libs.AndroidX.Core.Ktx,
    Libs.AndroidX.Lifecycle.ViewModel,
    Libs.AndroidX.Lifecycle.Livedata,
    Libs.AndroidX.Activity.Ktx,
    Libs.AndroidX.Fragment.Ktx,
    Libs.KotlinX.Datetime,
    Libs.Square.Kotlinpoet,
    Libs.Junit.Jupiter.Api,
  )
  runtimeOnly(Libs.Junit.Jupiter.Engine)
}
