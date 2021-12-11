publication.data {
  val baseVersion = "0.1.0"
  version = "$baseVersion-LOCAL"
  // Used to publish non-local versions of artifacts in CI environment
  versionInCI = "$baseVersion-SNAPSHOT"

  displayName = "Cloak"
  artifactId = "cloak"
  groupId = "com.meowool.teasn"
  url = "https://github.com/meowool-teasn/${rootProject.name}"
  vcs = "$url.git"
  developer {
    id = "rin"
    name = "Rin Orz"
    url = "https://github.com/RinOrz/"
  }
}

subprojects {
  optIn("com.meowool.cloak.internal.InternalCloakApi")
  dokka(DokkaFormat.Html) {
    outputDirectory.set(rootDir.resolve("docs/apis"))
  }
  kotlinExplicitApi()
}
