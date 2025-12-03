val scalaVersionUsed = "2.12.18"

lazy val root = project
  .in(file("."))
  .settings(
    name := "E-commerce",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scalaVersionUsed,
    fork := true,
    javaOptions ++= Seq(
      "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED"
    ),
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "3.5.1",
      "org.apache.spark" %% "spark-sql"  % "3.5.1"
    )
  )
