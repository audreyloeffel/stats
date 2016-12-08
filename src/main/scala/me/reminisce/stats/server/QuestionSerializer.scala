package me.reminisce.stats.server

import me.reminisce.stats.server.GameEntities.{GameQuestion, QuestionKind}
import org.json4s.JsonAST.{JField, JObject, JString}
import org.json4s._
import org.json4s.JsonDSL._

class QuestionSerializer extends CustomSerializer[GameQuestion](implicit formats => ( {
  case question: JObject =>
    val kind = (question \ "kind").extract[String]
    val tpe = (question \ "type").extract[String]
    val correct = (question \ "correct").extractOpt[Boolean]
    val timeSpent = (question \ "timeSpent").extractOpt[Int]

    GameQuestion(QuestionKind.withName(kind), tpe, correct, timeSpent)
}, {
  case GameQuestion(kind, tpe, correctOpt, timeSpentOpt) =>
    new JsonAssoc("kind" -> kind.toString) ~ ("type" -> tpe) ~ ("correct" -> correctOpt) ~ ("timeSpent" -> timeSpentOpt)
}))