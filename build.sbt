lazy val root = (project in file("."))
  .settings(
    name := """station-security""",
    version := "1.0",
    scalaVersion := "2.12.4",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor"    % "2.5.11",
      "com.typesafe.akka" %% "akka-stream"   % "2.5.11",
      "com.typesafe.akka" %% "akka-testkit"  % "2.5.11",
      "org.scalatest"     %% "scalatest"     % "3.0.5",
      "io.circe"          %% "circe-core"    % "0.9.3",
      "io.circe"          %% "circe-generic" % "0.9.3",
      "io.circe"          %% "circe-java8"   % "0.9.3",
      "io.circe"          %% "circe-parser"  % "0.9.3"
    )
  )