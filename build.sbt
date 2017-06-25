name := "persistence-pubsub-member"

version := "0.2"

scalaVersion := "2.12.1"

lazy val akkaVersion = "2.4.17"

resolvers ++= Seq("Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "scalaz-bintray" at "http://dl.bintray.com/sNcalaz/releases",
  "Product-Foundry at bintray" at "http://dl.bintray.com/productfoundry/maven")

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.23",
 // "cq.qos.logback" % "logback-classic" % "1.2.3",
  "com.marcom" %% "sportscommon" % "1.0",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.github.dnvriend" %% "akka-persistence-inmemory" % "2.4.17.1" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test")

