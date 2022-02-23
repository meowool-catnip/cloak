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
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

publication.data {
  val baseVersion = "0.1.0"
  version = "$baseVersion-LOCAL"
  // Used to publish non-local versions of artifacts in CI environment
  versionInCI = "$baseVersion-SNAPSHOT"

  displayName = "Cloak"
  artifactId = "cloak"
  groupId = "com.meowool.catnip"
  url = "https://github.com/meowool-catnip/${rootProject.name}"
  vcs = "$url.git"
  developer {
    id = "rin"
    name = "Rin Orz"
    url = "https://github.com/RinOrz/"
  }
}

subprojects {
  publication.data.artifactId = rootProject.name + "-" + name
  repositories.mavenLocal()
  optIn("com.meowool.cloak.internal.InternalCloakApi", "kotlin.RequiresOptIn")
  dokka(DokkaFormat.Html) { outputDirectory.set(rootDir.resolve("docs/apis")) }
}

registerLogic {
  project {
    val deps = arrayOf(Libs.Meowool.Toolkit.Sweekt)
    extensions.findByType<KotlinMultiplatformExtension>()?.apply {
      commonMainSourceSet.dependencies { implementationOf(*deps) }
    } ?: dependencies.implementationOf(*deps)
  }
}
