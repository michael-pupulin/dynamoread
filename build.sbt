

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.0"
import Keys._


val AkkaVersion = "2.6.20"
val AkkaHttpVersion = "10.2.10"
libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-dynamodb" % "4.0.0",
  "com.lightbend.akka" %% "akka-stream-alpakka-awslambda" % "4.0.0",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "org.slf4j" % "slf4j-api" % "2.0.1",
  "org.slf4j" % "slf4j-simple" % "2.0.1"
)
