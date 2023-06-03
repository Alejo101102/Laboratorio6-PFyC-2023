ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "Taller Paralelismo de Datos 2022-II"
  )
scalacOptions ++= Seq("-language:implicitConversions", "-deprecation")
libraryDependencies ++= Seq(
  ("com.storm-enroute" %% "scalameter-core" % "0.21").cross(CrossVersion.for3Use2_13),
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.3",
  "org.scalameta" %% "munit" % "0.7.26" % Test
)
libraryDependencies +=
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
libraryDependencies += "org.plotly-scala" %% "plotly-render" % "0.8.1"