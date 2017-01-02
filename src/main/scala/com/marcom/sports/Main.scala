package com.marcom.sports

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.cluster.client.ClusterClientReceptionist
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

object Main {
  def main(args: Array[String]): Unit = {
    if(args.size != 2){
        println("usage <hostname> <port>")
        System.exit(1)
    }
    val hostname = args(0)
    val port = args(1)
    implicit val timeout = Timeout(5 seconds)
    val config = ConfigFactory.parseString(
      s"""
         akka {
            remote {
               netty.tcp {
                   hostname="$hostname"
                   port=$port
               }
            }
         }
       """).withFallback(ConfigFactory.load())
    val systemName = "ClusterSystem"
    val system = ActorSystem(systemName,config)
    val cluster = Cluster(system)
    cluster.registerOnMemberUp {
      val sportingFixtureRegion = ClusterSharding(system).start(
        typeName = SportingFixture.shardName,
        entityProps = SportingFixture.props,
        settings = ClusterShardingSettings(system),
        extractEntityId = SportingFixture.idExtractor,
        extractShardId = SportingFixture.shardResolver)
        println(s"the region path is ${sportingFixtureRegion.path}")
      val sportingFixtureService = system.actorOf(SportingFixtureService.props)
    }
    cluster.registerOnMemberRemoved{
        system.registerOnTermination {
            System.exit(1)
        }

        system.terminate()

    }

  }

}
