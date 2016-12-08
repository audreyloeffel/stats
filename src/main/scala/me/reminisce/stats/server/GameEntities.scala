package me.reminisce.stats.server

import me.reminisce.stats.server.GameEntities.QuestionKind.QuestionKind
import me.reminisce.stats.server.domain.RestMessage
import reactivemongo.bson._

object GameEntities {

  trait EntityMessage

  case class GameQuestion(
    kind: QuestionKind, 
    `type`: String, 
    correct: Option[Boolean], 
    timeSpent: Option[Int])

  case class Game(
    _id: String,
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

  case class Tile(
    `type`: String,
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

  // Change the format of the Game that is stored in the DB
  implicit object GameWriter extends BSONDocumentWriter[Game] {
    def write(game: Game): BSONDocument = {
      val Game(id: String, player1: String, player2: String, player1Board: Board, player2Board: Board, status: String,
      player1Scores: Int, player2Scores: Int, wonBy: Int, creationTime: Long) = game

      val won = if (wonBy == 1) player1 else player2
      BSONDocument(
        "_id" -> id,
        "player1" -> player1,
        "player2" -> player2,
        s"${player1}_Board" -> player1Board,
        s"${player2}_Board" -> player2Board,
        "status" -> status,
        s"${player1}_Scores" -> player1Scores,
        s"${player2}_Scores" -> player2Scores,
        "wonBy" -> won,
        "creationTime" -> creationTime
      )
    }
  }

  implicit object GameReader extends BSONDocumentReader[Game] {
    def read(doc: BSONDocument): Game = {
      val id = doc.getAs[String]("_id").get
      val player1 = doc.getAs[String]("player1").get
      val player2 = doc.getAs[String]("player2").get
      val player1Board = doc.getAs[Board](s"${player1}_Board").get
      val player2Board = doc.getAs[Board](s"${player2}_Board").get
      val status = doc.getAs[String]("status").get
      val player1Scores = doc.getAs[Int](s"${player1}_Scores").get
      val player2Scores = doc.getAs[Int](s"${player2}_Scores").get
      val wonBy = if (doc.getAs[String]("wonBy").get == player1) 1 else 2
      val creationTime = doc.getAs[Long]("creationTime").get
      Game(id, player1, player2, player1Board, player2Board, status, player1Scores, player2Scores, wonBy, creationTime)
    }
  }

  
}