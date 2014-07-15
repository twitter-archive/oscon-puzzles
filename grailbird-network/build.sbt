scalaVersion := "2.10.4"

resolvers +=
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.3.1",
  "io.argonaut" %% "argonaut" % "6.0.4",
  "org.json4s" %% "json4s-jackson" % "3.2.10"
)
