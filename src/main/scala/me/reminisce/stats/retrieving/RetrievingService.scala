package me.reminisce.stats.retrieving

import akka.actor._
import com.github.nscala_time.time.Imports._
import me.reminisce.stats.model.DatabaseCollection
import me.reminisce.stats.model.Messages._
import me.reminisce.stats.model.RetrievingMessages._
import me.reminisce.stats.retrieving.RetrievingService._
import me.reminisce.stats.statistics.Responses._
import me.reminisce.stats.statistics.Stats.StatsEntities
import reactivemongo.api.DefaultDB
import reactivemongo.api.collections.bson._
import reactivemongo.bson.{BSONDateTime, BSONDocument}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object RetrievingService {
  def props(database: DefaultDB): Props =
    Props(new RetrievingService(database))

  def getStatistics(database: DefaultDB, userId: String, from: Option[DateTime], to: Option[DateTime], limit: Option[Int]): Future[List[StatsEntities]] = {
    val query = getQuery(userId, from, to)
    val collectionStats = database[BSONCollection](DatabaseCollection.statsCollection)
    limit match {
      case Some(max) =>
        collectionStats.find(query).sort(BSONDocument("date" -> -1)).cursor[StatsEntities]().collect[List](max)
      case None =>
        collectionStats.find(query).sort(BSONDocument("date" -> -1)).cursor[StatsEntities]().collect[List]()
    }
  }

  def getQuery(userId: String, from: Option[DateTime], to: Option[DateTime]): BSONDocument = {
    (from, to) match {
      case (Some(f), Some(t)) =>
        BSONDocument(
          "userId" -> userId,
          "date" -> BSONDocument(
            "$gte" -> BSONDateTime(f.getMillis),
            "$lte" -> BSONDateTime(t.getMillis)
          )
        )
      case (Some(f), None) =>
        BSONDocument(
          "userId" -> userId,
          "date" -> BSONDocument(
            "$gte" -> BSONDateTime(f.getMillis)
          )
        )
      case (None, Some(t)) =>
        BSONDocument(
          "userId" -> userId,
          "date" -> BSONDocument(
            "$lte" -> BSONDateTime(t.getMillis)
          )
        )
      case (None, None) =>
        BSONDocument(
          "userId" -> userId
        )
    }
  }
}

class RetrievingService(database: DefaultDB) extends Actor with ActorLogging {

  def receive: Receive = waitingForMessages

  def waitingForMessages: Receive = {
    case RetrieveStats(userId, from, to, limit) =>
      val client = sender
      val future = getStatistics(database, userId, from, to, limit)
      future.onComplete {
        case Success(stats) =>
          if (stats.isEmpty) {
            client ! UserNotFound(s"Statistics not found for $userId")
          } else {
            client ! StatsRetrieved(stats.map(responseFromStats))
          }
        case Failure(error) =>
          sender ! Abort
      }
    case o => log.info(s"[RS] Unexpected message ($o) received in waitingForMessages state")
  }
}
