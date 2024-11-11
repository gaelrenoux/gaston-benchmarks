ThisBuild / organization := "io.github.gaelrenoux"
ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "3.5.1"

name := "gaston-benchmarks"

lazy val `gaston-benchmarks` = (project in file("."))
  .aggregate(v0_5, v0_6, v0_7, v0_8, v0_9, v0_10, v1_0, v1_1)

ThisBuild / scalacOptions ++= Seq(
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",

  "-Werror", // Fail the compilation if there are any warnings.
  "-explain", // Explain type errors in more detail.

  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.

  "-Wsafe-init", // Wrap field accessors to throw an exception on uninitialized access.

  "-Wunused:explicits", // Warn if an explicit parameter is unused.
  "-Wunused:implicits", // Warn if an implicit parameter is unused.
  "-Wunused:imports", // Warn when imports are unused.
  "-Wunused:locals", // Warn if a local definition is unused.
  // "-Wunused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Wunused:privates", // Warn if a private member is unused.
  "-Wvalue-discard", // Warn when non-Unit expression results are unused.

  // "-XX:MaxInlineLevel=18", // see https://github.com/scala/bug/issues/11627#issuecomment-514619316 // check if still valid for Scala 3
)

fork := true
Global / cancelable := true

enablePlugins(JmhPlugin)


lazy val util = (project in file("util")).settings(
  name := "gaston-benchmark-utils",
  libraryDependencies += "com.google.guava" % "guava" % "33.2.1-jre"
)

lazy val v0_5 = (project in file("v0.5")).settings(
  name := "gaston-benchmark-0.5",
  libraryDependencies += "gael.renoux" % "gaston_2.13" % "0.5.0"
).dependsOn(util)

lazy val v0_6 = (project in file("v0.6")).settings(
  name := "gaston-benchmark-0.6",
  libraryDependencies += "gael.renoux" % "gaston_2.13" % "0.6.0"
).dependsOn(util)

lazy val v0_7 = (project in file("v0.7")).settings(
  name := "gaston-benchmark-0.7",
  libraryDependencies += "gael.renoux" % "gaston_2.13" % "0.7.0"
).dependsOn(util)

lazy val v0_8 = (project in file("v0.8")).settings(
  name := "gaston-benchmark-0.8",
  libraryDependencies += "gael.renoux" % "gaston_2.13" % "0.8.1"
).dependsOn(util)

lazy val v0_9 = (project in file("v0.9")).settings(
  name := "gaston-benchmark-0.9",
  libraryDependencies += "gael.renoux" % "gaston_2.13" % "0.9.0"
).dependsOn(util)

lazy val v0_10 = (project in file("v0.10")).settings(
  name := "gaston-benchmark-0.9",
  libraryDependencies += "gael.renoux" % "gaston_2.13" % "0.10.0"
).dependsOn(util)

lazy val v1_0 = (project in file("v1.0")).settings(
  name := "gaston-benchmark-1.0",
  libraryDependencies += "gael.renoux" %% "gaston" % "1.0.0",
  /* Adds Sonatype snapshots, required for Iron's snapshot version TODO Drop once we're on a definitive version */
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
).dependsOn(util)

lazy val v1_1 = (project in file("v1.1")).settings(
  name := "gaston-benchmark-1.1",
  libraryDependencies += "gael.renoux" %% "gaston" % "1.1.0",
  /* Adds Sonatype snapshots, required for Iron's snapshot version TODO Drop once we're on a definitive version */
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
).dependsOn(util)
