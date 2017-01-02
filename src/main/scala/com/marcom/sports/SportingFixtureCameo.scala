package com.marcom.sports

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.cluster.sharding.ClusterSharding
import com.marcom.sports.SportingFixture.{Persisted, SetFixtureState}
import com.marcom.sports.model.SportsFixture


class SportingFixtureCameo  extends Actor with ActorLogging{

      def receive = {
        case fix:SportsFixture => log.info("received a sports fixture , sending to the shard")
                    ClusterSharding(context.system).shardRegion(SportingFixture.shardName) ! SetFixtureState(fix.id.value, fix)
          context.become(awaitResponse(sender))

      }

      def awaitResponse(replyTo:ActorRef):Receive = {
        case Persisted => log.info("received a persistence acknowledgement")
          replyTo ! "persisted the fixture"
          context stop self
      }

}

object SportingFixtureCameo {
     def props = Props[SportingFixtureCameo]
}
