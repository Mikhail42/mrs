name := "mrs"

version := "0.1"

scalaVersion := "2.12.6"

val quilVersion = "2.5.4"
val mysqlVersion = "5.1.38"
val typesafeVersion = "1.3.3"
val slickVersion = "3.2.3"

val quilDeps = Seq("io.getquill" %% "quill" % quilVersion,
  "io.getquill" %% "quill-core" % quilVersion,
  "io.getquill" %% "quill-jdbc" % quilVersion)
libraryDependencies ++= quilDeps

libraryDependencies ++= Seq(
  "org.scalaxb" %% "scalaxb" % "1.7.0",
  "mysql" % "mysql-connector-java" % mysqlVersion,
  "com.typesafe" % "config" % typesafeVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion
)

val scalatestVersion = "3.0.5"
libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test"
)