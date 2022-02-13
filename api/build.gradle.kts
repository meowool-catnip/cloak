publication.data.artifactId = rootProject.name + "-" + project.name

plugins {
//  id(Plugins.Meowool.Sweekt)
}

androidLib()

configurations.all {
  resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

commonTarget {
  test.dependencies {
    implementationOf(
      Libs.Junit.Junit4.Runner,
      Libs.Kotest.Assertions.Core,
    )
  }
}

jvmTarget {
  configureTestRunTask {
    useJUnitPlatform()
  }
}

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