name := "caption-parser"

organization := "com.crowdscriber.captions"

version := "0.1.3"

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12", "2.12.6")

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.22"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.2.0" % Test


scalacOptions ++= Seq(
  "-feature",
  "-language:implicitConversions"
)
