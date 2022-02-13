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
  optIn("com.meowool.cloak.internal.InternalCloakApi", "kotlin.RequiresOptIn")
  dokka(DokkaFormat.Html) { outputDirectory.set(rootDir.resolve("docs/apis")) }
  repositories.mavenLocal()
}

registerLogic {
  project {
    val deps = arrayOf(Libs.Meowool.Toolkit.Sweekt)
    extensions.findByType<KotlinMultiplatformExtension>()?.apply {
      commonMainSourceSet.dependencies { implementationOf(*deps) }
    } ?: dependencies.implementationOf(*deps)
  }
}