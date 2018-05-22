name := "caption-parser"

organization := "com.crowdscriber.captions"

version := "0.1.4"

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12", "2.12.6")

// POM settings for Sonatype
homepage := Some(url("https://github.com/Crowdscriber/caption-parser/blob/master/README.md"))
scmInfo := Some(ScmInfo(url("https://github.com/Crowdscriber/caption-parser"),
"git@github.com:Crowdscriber/caption-parser.git"))
developers := List(Developer(
  id    = "ctataryn",
  name  = "Craig Tataryn",
  email = "craiger@tataryn.net",
  url   = url("https://github.com/ctataryn")
))
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true

// Add sonatype repository settings
publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

scalacOptions ++= Seq(
  "-feature",
  "-language:implicitConversions"
)


libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.22"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.2.0" % Test
