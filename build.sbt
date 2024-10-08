ThisBuild / organization := "io.github.gaelrenoux"
ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.13.6"

name := "gaston-benchmarks"

lazy val `gaston-benchmarks` = (project in file("."))
  .aggregate(v0_5, v0_6, v0_7, v0_8, vSnapshot)

ThisBuild / scalacOptions ++= Seq(

  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",

  // "-XX:MaxInlineLevel=18", // see https://github.com/scala/bug/issues/11627#issuecomment-514619316

  "-explaintypes", // Explain type errors in more detail.
  "-Werror", // Fail the compilation if there are any warnings.

  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
  //  "-Xdev", // Indicates user is a developer - issue warnings about anything which seems amiss

  "-Wdead-code", // Warn when dead code is identified.
  "-Wextra-implicit", // Warn when more than one implicit parameter section is defined.
  "-Wmacros:before", // Enable lint warnings on macro expansions. Default: before, help to list choices.
  "-Wnumeric-widen", // Warn when numerics are widened.
  "-Woctal-literal", // Warn on obsolete octal syntax.
  // "-Wself-implicit", // this detects to much (see https://github.com/scala/bug/issues/10760 for original justification)
  "-Wunused:explicits", // Warn if an explicit parameter is unused.
  "-Wunused:implicits", // Warn if an implicit parameter is unused.
  "-Wunused:imports", // Warn when imports are unused.
  "-Wunused:locals", // Warn if a local definition is unused.
  "-Wunused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Wunused:privates", // Warn if a private member is unused.
  "-Wvalue-discard", // Warn when non-Unit expression results are unused.

  // "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver. // this is fine
  "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
  "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
  "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
  "-Xlint:private-shadow", //  A private field (or class parameter) shadows a superclass field.
  "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
  "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:option-implicit", // Option.apply used implicit view.
  "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
  // "-Xlint:package-object-classes", // Class or object defined in package object. // this is fine
  "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
  "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:unused", // Enable -Ywarn-unused:imports,privates,locals,implicits.
  "-Xlint:nonlocal-return", // A return statement used an exception for flow control.
  "-Xlint:implicit-not-found", // Check @implicitNotFound and @implicitAmbiguous messages.
  "-Xlint:serial", // @SerialVersionUID on traits and non-serializable classes.
  "-Xlint:valpattern", // Enable pattern checks in val definitions.
  "-Xlint:eta-zero", // Warn on eta-expansion (rather than auto-application) of zero-ary method.
  "-Xlint:eta-sam", // Warn on eta-expansion to meet a Java-defined functional interface that is not explicitly annotated with @FunctionalInterface.
  "-Xlint:deprecation" // Enable linted deprecations.
)

ThisBuild / libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.8" % "test"
)

ThisBuild / Test / fork := true
ThisBuild / Test / testForkedParallel := true // run tests in parallel on the forked JVM
ThisBuild / Test / testOptions += Tests.Argument("-oD") // show test duration
ThisBuild / Test / testOptions += Tests.Argument("-oD") // show test duration

/* Stays inside the sbt console when we press "ctrl-c" in tests" */
Global / cancelable := true

enablePlugins(JmhPlugin)


lazy val util = (project in file("util")).settings(
  name := "gaston-benchmark-utils",
  libraryDependencies += "com.google.guava" % "guava" % "33.2.1-jre"
)

lazy val v0_5 = (project in file("v0.5")).settings(
  name := "gaston-benchmark-0.5",
  libraryDependencies += "gael.renoux" %% "gaston" % "0.5.0"
).dependsOn(util)

lazy val v0_6 = (project in file("v0.6")).settings(
  name := "gaston-benchmark-0.6",
  libraryDependencies += "gael.renoux" %% "gaston" % "0.6.0"
).dependsOn(util)

lazy val v0_7 = (project in file("v0.7")).settings(
  name := "gaston-benchmark-0.7",
  libraryDependencies += "gael.renoux" %% "gaston" % "0.7.0"
).dependsOn(util)

lazy val v0_8 = (project in file("v0.8")).settings(
  name := "gaston-benchmark-0.8",
  libraryDependencies += "gael.renoux" %% "gaston" % "0.8.1"
).dependsOn(util)

lazy val v0_9 = (project in file("v0.9")).settings(
  name := "gaston-benchmark-0.9",
  libraryDependencies += "gael.renoux" %% "gaston" % "0.9.0"
).dependsOn(util)

lazy val vSnapshot = (project in file("vSnapshot")).settings(
  name := "gaston-benchmark-snapshot",
  libraryDependencies += "gael.renoux" %% "gaston" % "0.10.0"
).dependsOn(util)
