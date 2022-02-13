plugins { kotlin }

dependencies {
  testImplementationOf(
    project(Projects.Api),
    Libs.Junit.Jupiter.Api,
    Libs.Kotest.Assertions.Core,
  )
  testRuntimeOnly(Libs.Junit.Jupiter.Engine)
}

tasks.test { useJUnitPlatform() }

sourceSets.test.java.srcDir("kotlin")
