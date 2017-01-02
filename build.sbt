name := "persistence-cluster-member"

version := "0.2"

scalaVersion := "2.11.8"

lazy val akkaVersion = "2.4.16"

resolvers ++= Seq("Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Product-Foundry at bintray" at "http://dl.bintray.com/productfoundry/maven")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
  "com.productfoundry" %% "akka-persistence-couchbase" % "0.4.1",
  "com.marcom" %% "sportscommon" % "1.0",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test")

