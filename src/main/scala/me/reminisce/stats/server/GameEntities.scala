package me.reminisce.stats.server

import me.reminisce.stats.server.GameEntities.QuestionKind.QuestionKind
import me.reminisce.stats.server.domain.RestMessage
import reactivemongo.bson._

object GameEntities {

  trait EntityMessage

  case class GameQuestion(kind: QuestionKind, `type`: String, correct: Option[Boolean])

  case class Game(_id: String,
                  player1: String,
                  player2: String,
                  player1Board: Board,
                  player2Board: Board,
                  status: String,
                  player1Score: Int,
                  player2Score: Int,
                  wonBy: Int,
                  creationTime: Long
                 ) {
    override def toString: String = s"GAME: players: $player1($player1Score) vs $player2($player2Score) : winner: $wonBy"
  }

  case class Board(userId: String, tiles: List[Tile], _id: String) extends RestMessage

  case class Tile(`type`: String,
                  _id: String,
                  question1: GameQuestion,
                  question2: GameQuestion,
                  question3: GameQuestion,
                  score: Int,
                  answered: Boolean,
                  disabled: Boolean) extends RestMessage

  object QuestionKind extends Enumeration {
    type QuestionKind = Value
    val MultipleChoice = Value("MultipleChoice")
    val Timeline = Value("Timeline")
    val Geolocation = Value("Geolocation")
    val Order = Value("Order")
    val Misc = Value("Misc")
  }

  implicit object QuestionKindWriter extends BSONWriter[QuestionKind, BSONString] {
    def write(t: QuestionKind): BSONString = BSONString(t.toString)
  }

  implicit object QuestionKindReader extends BSONReader[BSONValue, QuestionKind] {
    def read(bson: BSONValue): QuestionKind = bson match {
      case BSONString(s) => QuestionKind.withName(s)
    }
  }

  implicit val gameQuestionHandler = Macros.handler[GameQuestion]
  implicit val tileHandler = Macros.handler[Tile]
  implicit val boardHandler = Macros.handler[Board]
  implicit val gameHandler = Macros.handler[Game]
  
}