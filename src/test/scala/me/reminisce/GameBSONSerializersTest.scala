package me.reminisce

import me.reminisce.stats.server.GameEntities._
import org.scalatest.FunSuite
import reactivemongo.bson._


class GameBSONSerializersTest extends FunSuite {
  
  // ***** GAMEQUESTION *****
  val docGameQuestion = BSONDocument(
    "type" -> "GeoWhatCoordinatesWereYouAt",
    "kind" -> QuestionKind.Geolocation,
    "correct" -> true,
    "timeSpent" -> 5000
  )
  val gameQuestion = GameQuestion(QuestionKind.Geolocation, "GeoWhatCoordinatesWereYouAt", Some(true), Some(5000))

  test("QuestionWrite") {
    val bson = BSON.writeDocument(docGameQuestion)
    assert(bson.getAs[Boolean]("correct") == docGameQuestion.getAs[Boolean]("correct"))
    assert(bson.getAs[String]("type") == docGameQuestion.getAs[String]("type"))
    assert(bson.getAs[String]("kind") == docGameQuestion.getAs[String]("kind"))
    assert(bson.getAs[Long]("timeSpent") == docGameQuestion.getAs[Long]("timeSpent"))
  }

  test("QuestionRead") {

    val result = docGameQuestion.as[GameQuestion]
    assert(result == gameQuestion)
  }

  test("QuestionWriteRead") {

    val bson = BSON.writeDocument(gameQuestion)
    val result = bson.as[GameQuestion]
    assert(gameQuestion == result)
  }

  // ***** TILE *****

  val docTile = BSONDocument(
    "type" -> "Misc",
    "_id" -> "id12345",
    "question1" -> BSON.write(gameQuestion),
    "question2" -> docGameQuestion,
    "question3" -> docGameQuestion,
    "score" -> 2341,
    "answered" -> true,
    "disabled" -> false
  )
  val tile = Tile(
    "Misc",
    "id12345",
    gameQuestion,
    gameQuestion,
    gameQuestion,
    2341, answered = true, disabled = false
  )
  test("TileWrite") {
    val bson = BSON.writeDocument(tile)
    assert(docTile.getAs[String]("_id") == bson.getAs[String]("_id"))
    assert(docTile.getAs[GameQuestion]("question1") == bson.getAs[GameQuestion]("question1"))
    assert(docTile.getAs[GameQuestion]("question2") == bson.getAs[GameQuestion]("question2"))
    assert(docTile.getAs[GameQuestion]("question3") == bson.getAs[GameQuestion]("question3"))
    assert(docTile.getAs[Int]("score") == bson.getAs[Int]("score"))
    assert(docTile.getAs[Boolean]("answered") == bson.getAs[Boolean]("answered"))
    assert(docTile.getAs[Boolean]("disabled") == bson.getAs[Boolean]("disabled"))
  }

  test("TileRead") {
    val result = docTile.as[Tile]
    assert(result == tile)
  }

  test("TileWriteRead") {
    val bson = BSON.writeDocument(docTile)
    val result = bson.as[Tile]
    assert(tile == result)
  }

}