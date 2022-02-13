plugins { id(Plugins.Android.Junit5) }

androidKotlinLib {
  sourceSets {
    main.manifest.srcFile("AndroidManifest.xml")
    test {
      java.srcDir("sources")
      resources.srcDir("resources")
    }
    arrayOf("jvm.src", "android.src").forEach {
      // Get and save the paths of all parent project main source sets to the resource directory
      file("resources").resolve(it).writeText(parent!!.ext[it].toString())
    }
  }
}

dependencies {
  testImplementationOf(
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
  testRuntimeOnly(Libs.Junit.Jupiter.Engine)
}