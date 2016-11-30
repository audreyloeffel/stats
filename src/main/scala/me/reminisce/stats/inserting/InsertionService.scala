package me.reminisce.stats.inserting

import akka.actor._
import reactivemongo.api.DefaultDB
import me.reminisce.stats.computing.ComputationService
import me.reminisce.stats.model.ComputationMessages._
import me.reminisce.stats.model.Messages._
import me.reminisce.stats.model.InsertionMessages._

object InsertionService {
  def props(database: DefaultDB):Props =
    Props(new InsertionService(database))
}

class InsertionService(database: DefaultDB) extends Actor with ActorLogging {

  def receive: Receive = {
    case msg @ InsertEntity(game) => 
      log.info(s"Received game: $game")
      val worker = context.actorOf(InsertionWorker.props(database))
      worker ! msg
      context.become(waitingForMessages(sender))
    case any => log.error(s"Unexpected message received in insertion service: $any")
    }

  def waitingForMessages(client: ActorRef): Receive = {
    case Inserted(ids) => 
      ids.foreach{ id =>
        client ! InsertionDone()
        val worker = context.actorOf(ComputationService.props(database))
        worker ! ComputeStatistics(id)}
      sender ! PoisonPill 
    case Done => 
      client ! InsertionDone()
      sender ! PoisonPill
    case Abort => 
      client ! InsertionAbort()
      sender ! PoisonPill
    case any =>
      log.error(s"Unexpected message received in insertion service: $any")
  }
}
