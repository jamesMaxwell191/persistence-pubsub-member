package com.marcom.sports

import akka.actor.{Props, ActorLogging}
import akka.cluster.sharding.ShardRegion
import akka.persistence.{SnapshotOffer, PersistentActor}
import com.marcom.sports.model.SportsFixture
import com.marcom.sports.SportingFixture._

class SportingFixture extends PersistentActor with ActorLogging {

  override def persistenceId = self.path.name

  var state:SportsFixture = _

  val receiveRecover: Receive = {
    case evt: SportsFixture => log.info(s"replaying event $evt") ;updateState(evt)

    case SnapshotOffer(_, snapshot: SportsFixture) => state = snapshot
  }

  val receiveCommand: Receive = {
    case SetState(fixture) => log.info(s"setting the fixture state at path ${self.path.name}")
            persist(fixture)(updateState)

    case GetState => sender() ! state
  }

  def updateState(sportsFixture: SportsFixture): Unit = {
    state = sportsFixture
    sender ! Persisted
  }

  def logPath() = log.info(self.path.name)


}

object SportingFixture {

  trait Cmd

  case object GetState extends Cmd

  case object Persisted

  case class SetState(fixture:SportsFixture) extends Cmd

  def props = Props[SportingFixture]

  case class SetFixtureState(id:String,sportsFixture: SportsFixture)


  case class GetFixtureDetails(fixtureId: String)

  val idExtractor: ShardRegion.ExtractEntityId = {
    case SetFixtureState(id, fixture) => (id, SetState(fixture))
    case GetFixtureDetails(id) => (id, GetState)
  }

  val shardResolver: ShardRegion.ExtractShardId =  {
    case SetFixtureState(id, _) => (math.abs(id.hashCode) % 100).toString
    case GetFixtureDetails(id) => (math.abs(id.hashCode) % 100).toString
  }

  val shardName: String = "SportingFixture"

}
