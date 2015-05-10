name := "omnibus"

version := "0.1"

scalaVersion := "2.11.5"

libraryDependencies := Seq(
  "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-M4",
  "com.typesafe.akka" %% "akka-http-experimental" % "1.0-M4",
  "com.typesafe.akka" %% "akka-http-core-experimental" % "1.0-M4",
  "com.google.protobuf" % "protobuf-java" % "2.5.0",
  "com.github.nscala-time" %% "nscala-time" % "2.0.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
