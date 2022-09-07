ThisBuild / scalaVersion := "2.13.8"
ThisBuild / organization := "com.werk"
scapegoatVersion in ThisBuild := "1.4.15"
scalacOptions in Scapegoat += "-P:scapegoat:reports:html"

lazy val mrlacnik = (project in file("."))
  .settings(
    name := "mrlacnik",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.7" % Test,
      "com.typesafe.play" %% "play-json" % "2.9.2",
      "com.eed3si9n" %% "gigahorse-okhttp" % "0.5.0",
      "net.ruippeixotog" %% "scala-scraper" % "3.0.0",
      "io.crashbox" %%% "argparse" % "0.16.1"
    )
  )

// sbt> graalvm-native-image:packageBin
// https://www.vandebron.tech/blog/building-native-images-and-compiling-with-graalvm-and-sbt
enablePlugins(GraalVMNativeImagePlugin)
graalVMNativeImageOptions ++= Seq(
  "--native-image-info",
  "--verbose",
  "--no-fallback",
  "--enable-url-protocols=http,https",
  "--report-unsupported-elements-at-runtime"
)
