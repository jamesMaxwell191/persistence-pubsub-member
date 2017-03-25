package com.marcom.sports

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Subscribe, SubscribeAck}
import akka.cluster.sharding.ClusterSharding
import com.marcom.sports.SportingFixture.SetFixtureState
import com.marcom.sports.model.SportsFixture

/**
  * Created by douglas on 24/01/16.
  */
class SportingFixtureService extends Actor with ActorLogging{

     val mediator = DistributedPubSub(context.system).mediator

     mediator ! Subscribe("sportscontent",self)

     def receive = {
       case fix:SportsFixture => log.info(s"received a sporting fixture $fix")
           ClusterSharding(context.system).shardRegion(SportingFixture.shardName) ! SetFixtureState(fix.id.toString, fix)
       case SubscribeAck(Subscribe("content", None, `self`)) => log.info("subscribing to sporting content");
     }
}

object SportingFixtureService {
    def props = Props[SportingFixtureService]
}
