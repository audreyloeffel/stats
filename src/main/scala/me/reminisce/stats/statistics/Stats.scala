package me.reminisce.stats.statistics


import com.github.nscala_time.time.Imports._
import me.reminisce.stats.statistics.Utils._
import reactivemongo.bson._

object Stats {

  case class StatsEntities (
    id: BSONObjectID,
    userId: String,
    date: DateTime = DateTime.now,
    amount: Int,
    win: Int,
    loss: Int,
    tie: Int,
    rivals: Set[Rival],
    questionsByType: QuestionsByType
    )

  case class QuestionStats(
    amount: Int,
    correct: Double,
    wrong: Double,
    avoid: Int
    )

  case class Rival(
    rivalId: String,
    number: Int
    )

  case class QuestionsByType(
    multipleChoice: QuestionStats,
    timeline: QuestionStats,
    geolocation: QuestionStats,
    order: QuestionStats
    )

  implicit val questionStatsHandler: BSONHandler[BSONDocument, QuestionStats] = Macros.handler[QuestionStats]
  implicit val questionsByTypeHandler: BSONHandler[BSONDocument, QuestionsByType] = Macros.handler[QuestionsByType]
  implicit val rivalHandler: BSONHandler[BSONDocument, Rival] = Macros.handler[Rival]
  implicit val statsHandler = Macros.handler[StatsEntities]
}