import sbt._

import Keys._
import AndroidKeys._

object General {
  val settings = Defaults.defaultSettings ++ Seq (
    name := "My_Android_Project",
    version := "0.1",
    versionCode := 0,
    scalaVersion := "2.10.0",
    javacOptions ++= Seq("-source", "1.6", "-target", "1.6"),

    platformName in Android := "android-10"
  )

  val proguardSettings = Seq (
    useProguard in Android := true
  )

  lazy val fullAndroidSettings =
    General.settings ++
    AndroidProject.androidSettings ++
    TypedResources.settings ++
    proguardSettings ++
    AndroidManifestGenerator.settings ++
    AndroidMarketPublish.settings ++ Seq (
      keyalias in Android := "change-me",
      libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0.M5b"
    )
}

object AndroidBuild extends Build {
  lazy val main = Project (
    "My_Android_Project",
    file("."),
    settings = General.fullAndroidSettings
  )

  lazy val tests = Project (
    "tests",
    file("tests"),
    settings = General.settings ++
               AndroidTest.androidSettings ++
               General.proguardSettings ++ Seq (
      name := "My_Android_ProjectTests"
    )
  ) dependsOn main
}
