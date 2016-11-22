package me.reminisce.stats.statistics

import com.github.nscala_time.time.Imports._
import me.reminisce.stats.statistics.Stats.{QuestionsByType, Rival, StatsEntities}

object Responses {
  case class StatsResponse(
    userId: String,
    date: DateTime = DateTime.now,
    amount: Int,
    win: Int,
    loss: Int,
    tie: Int,
    rivals: Set[Rival],
    questionsByType: QuestionsByType
  )

  def responseFromStats(stats: StatsEntities): StatsResponse = stats match {
    case StatsEntities(_, userId, date, amount, win, loss, tie, rivals, questionsByType) =>
      StatsResponse(userId, date, amount, win, loss, tie, rivals, questionsByType)
  }
}
