name := "blacklist"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11",
  "com.google.guava" % "guava" % "23.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.20",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.11" % Test,
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)