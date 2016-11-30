package me.reminisce.stats.inserting

import akka.actor._
import me.reminisce.stats.server.GameEntities._
import me.reminisce.stats.model.DatabaseCollection
import me.reminisce.stats.model.Messages._
import reactivemongo.api.DefaultDB
import reactivemongo.api.collections.bson._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object InsertionWorker {
  def props(database: DefaultDB):Props =
    Props(new InsertionWorker(database))
 }

class InsertionWorker(database: DefaultDB) extends Actor with ActorLogging {
  import me.reminisce.stats.model.InsertionMessages._

  def receive : Receive = {
    case InsertEntity(entity: Game) => insertInDb(entity)
    case o =>
      log.info(s"Unexpected message received in InsertionWorker: $o")
  }

  /*
   * Insert a Game in the database and send the id's of players to the client
   */
  def insertInDb(entity: Game) = {  
    val col = database[BSONCollection](DatabaseCollection.gameCollection)
    val future = col.insert(entity)

    future.onComplete {
      case Failure(e) => 
        context.parent ! Abort
        log.error(s"Error while inserting a game: $e")
      case Success(lastError) => { 
        val toCompute = List(entity.player1, entity.player2)
        context.parent ! Inserted(toCompute)
      }
    }
  }
}
