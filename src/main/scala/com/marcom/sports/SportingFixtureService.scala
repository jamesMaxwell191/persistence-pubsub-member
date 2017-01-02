package com.marcom.sports

import akka.actor.{Actor, ActorLogging, Props}
import com.marcom.sports.model.SportsFixture

/**
  * Created by douglas on 24/01/16.
  */
class SportingFixtureService extends Actor with ActorLogging{

     var count = 0

     def receive = {
       case fix:SportsFixture => log.info("received a sporting fixture")
         count = count + 1
         val cameo = context.actorOf(SportingFixtureCameo.props,s"cameo-$count")
             cameo forward fix
       case s:String => sender ! s"got the report"
     }
}

object SportingFixtureService {

    def props = Props[SportingFixtureService]
}
