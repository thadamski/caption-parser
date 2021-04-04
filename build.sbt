name := "caption-parser"

organization := "com.crowdscriber.captions"

version := "0.1.6"

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12", "2.12.6", "2.13.0")

scalacOptions ++= Seq(
  "-encoding", "utf8", // Option and arguments on same line
  "-Xfatal-warnings",  // New lines for each options
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)

// POM settings for Sonatype
sonatypeProfileName := "com.crowdscriber"
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
publishConfiguration := publishConfiguration.value.withOverwrite(true)

// Add sonatype repository settings
publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)


libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.22"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.7.0" % Test
