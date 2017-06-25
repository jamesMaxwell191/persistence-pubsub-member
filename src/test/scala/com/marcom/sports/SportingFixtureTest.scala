package com.marcom.sports

import java.util.UUID
import java.util.concurrent.CountDownLatch

import scala.concurrent.duration._
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.cluster.Cluster
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.pattern.{Backoff, BackoffSupervisor}
import akka.testkit.{ImplicitSender, TestKit}
import com.marcom.sports.SportingFixture.{CreateFixture, GetFixtureDetails, SetFixtureState, SetState}
import com.marcom.sports.model.SportsFixture
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}


class SportingFixtureTest(_system:ActorSystem) extends TestKit(_system) with ImplicitSender
           with WordSpecLike with BeforeAndAfterAll with Matchers {

  val latch = new CountDownLatch(1)

  val cluster = Cluster(system)

  cluster.registerOnMemberUp{
    val supervisor = BackoffSupervisor.props(
      Backoff.onStop(
        SportingFixture.props,
        childName = "entity",
        minBackoff = 3.seconds,
        maxBackoff = 30.seconds,
        randomFactor = 0.2 // adds 20% "noise" to vary the intervals slightly
      ))
    val sports = ClusterSharding(system).start(
      typeName = SportingFixture.shardName,
      entityProps = supervisor,
      settings = ClusterShardingSettings(system),
      extractEntityId = SportingFixture.idExtractor,
      extractShardId = SportingFixture.shardResolver)
    println("cluster node is up")
    latch.countDown()
  }
  cluster.registerOnMemberRemoved{
    system.registerOnTermination {
      System.exit(1)
    }

    system.terminate()

  }

  cluster.join(cluster.selfAddress)


  def this() = this(ActorSystem("testSystem",ConfigFactory.load("application-test.conf")))

  override protected def afterAll(): Unit = {
    cluster.leave(cluster.selfAddress)
    TestKit.shutdownActorSystem(system)
  }

  "an sporting fixture" should {
     latch.await()
     "persist" in {
       println("running the test .................")
       val uuid = UUID.fromString("fffdb83b-922e-4ae0-8dc5-6f861a0c1508")
        val fixture = SportsFixture(uuid,"Rangers vs Celtic")
        ClusterSharding(system).shardRegion(SportingFixture.shardName) ! SetFixtureState(uuid.toString,fixture)
        expectMsg(SportingFixture.Persisted)
        ClusterSharding(system).shardRegion(SportingFixture.shardName) ! GetFixtureDetails(uuid.toString)
        expectMsgClass(classOf[SportsFixture])
     }
  }
}

class EchoActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case _ => log.info("received a message!!!!!!!!!!!!!")
  }
}

object EchoActor {

  def props = Props[EchoActor]
}
