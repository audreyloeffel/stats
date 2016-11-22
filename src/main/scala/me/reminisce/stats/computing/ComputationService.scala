package me.reminisce.stats.computing

import akka.actor._
import com.github.nscala_time.time.Imports._
import me.reminisce.stats.model.ComputationMessages._
import me.reminisce.stats.model.DatabaseCollection
import me.reminisce.stats.model.Messages._
import me.reminisce.stats.server.GameEntities.QuestionKind.QuestionKind
import me.reminisce.stats.server.GameEntities._
import me.reminisce.stats.statistics.Stats._
import me.reminisce.stats.statistics.Utils._
import org.joda.time.DateTime
import reactivemongo.api.DefaultDB
import reactivemongo.api.collections.bson._
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ComputationService {
  def props(database: DefaultDB): Props =
    Props(new ComputationService(database))
}

class ComputationService(database: DefaultDB) extends Actor with ActorLogging {

  import scala.concurrent.ExecutionContext.Implicits.global

  def receive: Receive = waitingRequest

  val collectionGame = database[BSONCollection](DatabaseCollection.gameCollection)
  val collectionStats = database[BSONCollection](DatabaseCollection.statsCollection)

  def waitingRequest: Receive = {
    case ComputeStatistics(userId) =>
      compute(userId)
    case o =>
      log.info(s"Unexpected message $o received in computation service")
  }

  def compute(userId: String): Unit = {

    val userScore = s"${userId}_Scores"
    val queryGame = BSONDocument(
      "status" -> "ended",
      userScore -> BSONDocument(
        "$exists" -> true
      )
    )
    val g: Future[List[Game]] = collectionGame.find(queryGame).cursor[Game]().collect[List]()
    g.onComplete {
      case Success(games) =>
        val stats = aggregate(games, userId)
        insertOrUpdate(userId, stats)
      case Failure(e) =>
        log.error(s"Could not reach the database: $e")
        context.parent ! Abort
    }
  }

  def insertOrUpdate(userId: String, stats: StatsEntities): Unit = {

    val now: DateTime = DateTime.now
    val midnightToday = new DateTime(now.getYear, now.getMonthOfYear, now.getDayOfMonth, 0, 0, 0)
    val queryStats = BSONDocument(
      "userId" -> userId
    )
    val futureOnStats: Future[List[StatsEntities]] = collectionStats.find(queryStats).cursor[StatsEntities]().collect[List]()
    futureOnStats.onComplete {
      case Success(existingStats) =>
        val todayStats = existingStats.filter(x => x.date > midnightToday)
        if (todayStats.isEmpty) {
          val future = collectionStats.insert(stats)
          future.onComplete {
            case Failure(e) =>
              context.parent ! Abort
            case Success(lastError) =>
              context.parent ! Done
          }
        } else {
          val selector = BSONDocument("_id" -> todayStats.head.id)
          val StatsEntities(_, _, date, amount, win, loss, tie, rivals, questionsByType) = stats

          val modifier = BSONDocument(
            "$set" -> BSONDocument(
              "date" -> date,
              "amount" -> amount,
              "win" -> win,
              "loss" -> loss,
              "tie" -> tie,
              "rivals" -> rivals,
              "questionsByType" -> questionsByType))

          val futureUpdate = collectionStats.update(selector, modifier)
          futureUpdate.onComplete {
            case Success(lastError) =>
              context.parent ! Done
            case Failure(e) =>
              log.error(s"Failed to update stats: $e")
              context.parent ! Abort
          }
        }
      case Failure(e) =>
        log.error(s"Could not reach the database: $e")
        context.parent ! Abort
    }
  }

  def aggregate(games: List[Game], userId: String): StatsEntities = {
    val (win, loss, tie, amount): (Int, Int, Int, Int) = games.foldLeft[(Int, Int, Int, Int)]((0, 0, 0, 0)) {
      case ((w, l, t, a), Game(_, player1, _, _, _, _, player1Score, player2Score, _, _)) =>
        val (score, rival) = if (player1 == userId) (player1Score, player2Score) else (player2Score, player1Score)
        if (score > rival) {
          (w + 1, l, t, a + 1)
        } else {
          if (score < rival) {
            (w, l + 1, t, a + 1)
          } else {
            (w, l, t + 1, a + 1)
          }
        }
    }
    val rivalsList: List[String] = games.foldLeft[List[String]](List()) {
      case (rList, Game(_, player1, player2, _, _, _, _, _, _, _)) =>
        if (player1 == userId) player2 :: rList else player1 :: rList
    }
    val rivals = rivalsList.groupBy(s => s).map(s => Rival(s._1, s._2.length)).toSet

    val allQuestionsPairedWithScore: List[(Boolean, Double, GameQuestion)] = games.foldLeft[List[(Boolean, Double, GameQuestion)]](List[(Boolean, Double, GameQuestion)]()) {
      case (t, Game(_, player1, _, player1Board, player2Board, _, _, _, _, _)) =>

        val pairQuestionWithScore = (tiles: List[Tile]) => {

          tiles.foldLeft[List[(Boolean, Double, GameQuestion)]](List()) {
            case (l, Tile(_, _, q1, q2, q3, scoreTile, answered, disable)) =>
              if (!(disable & !answered)) {
                val questions = List(q1, q2, q3)
                l ++ questions.map { x =>
                  x.correct match {
                    case Some(isCorrect) =>
                      if (isCorrect) {
                        (answered, 1.0, x)
                      } else {
                        (answered, 0.0, x)
                      }
                    case None => (false, 0.0, x)
                  }
                }
              } else {
                l
              }
          }
        }

        if (userId == player1) {
          val questionsList: List[(Boolean, Double, GameQuestion)] = pairQuestionWithScore(player1Board.tiles)
          t ++ questionsList
        }
        else {
          val questionsList: List[(Boolean, Double, GameQuestion)] = pairQuestionWithScore(player2Board.tiles)
          t ++ questionsList
        }
    }

    val questionsGroupedByKind: Map[QuestionKind, List[(Boolean, Double, GameQuestion)]] = allQuestionsPairedWithScore.groupBy { case (a, s, q) => q.kind }

    val scoringForEachKind = questionsGroupedByKind.map(kind => (kind._1, kind._2.foldLeft[(Int, Double, Double, Int)]((0, 0, 0, 0)) {
      case ((a, c, w, av), (answer, score, question)) =>
        if (answer) {
          (a + 1, c + score, w + (1 - score), av)
        } else {
          (a, c, w, av + 1)
        }
    }
      )).map { case (k, (amount: Int, correct: Double, wrong: Double, avoid: Int)) => (k, QuestionStats(amount, correct, wrong, avoid)) }

    val questionsByType = QuestionsByType(
      scoringForEachKind.getOrElse(QuestionKind.MultipleChoice, QuestionStats(0, 0, 0, 0)),
      scoringForEachKind.getOrElse(QuestionKind.Timeline, QuestionStats(0, 0, 0, 0)),
      scoringForEachKind.getOrElse(QuestionKind.Geolocation, QuestionStats(0, 0, 0, 0)),
      scoringForEachKind.getOrElse(QuestionKind.Order, QuestionStats(0, 0, 0, 0))
    )

    val id = BSONObjectID.generate
    val stats = StatsEntities(id, userId, DateTime.now, amount, win, loss, tie, rivals, questionsByType)
    stats
  }
}