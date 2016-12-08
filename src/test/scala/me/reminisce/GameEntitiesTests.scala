package me.reminisce

import java.io.InputStream

import me.reminisce.stats.server.GameEntities._
import me.reminisce.stats.server.QuestionSerializer
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.scalatest.FunSuite

class GameEntitiesTests extends FunSuite {

  def assertEquals(expected: Game, actual: Game): Unit = {

    assert(expected._id == actual._id)
    assert(expected.creationTime == actual.creationTime)
    assert(expected.player1 == actual.player1)
    assert(expected.player2 == actual.player2)
    assert(expected.player1Score == actual.player1Score)
    assert(expected.player2Score == actual.player2Score)
    assert(expected.status == actual.status)
    assert(expected.wonBy == actual.wonBy)

    assertEquals(expected.player1Board, actual.player1Board)
    assertEquals(expected.player2Board, actual.player2Board)
  }

  def assertEquals(expected: Board, actual: Board): Unit = {
    assert(expected._id == actual._id)
    assert(expected.userId == actual.userId)
    assert(expected.tiles.length == actual.tiles.length)

    expected.tiles.zip(actual.tiles).foreach {
      case (expectedTile, actualTile) =>
        assertEquals(expectedTile, actualTile)
    }
  }

  def assertEquals(expected: Tile, actual: Tile): Unit = {
    assert(expected.`type` == actual.`type`)
    assert(expected._id == actual._id)
    assertEquals(expected.question1, actual.question1)
    assertEquals(expected.question2, actual.question2)
    assertEquals(expected.question3, actual.question3)
    assert(expected.score == actual.score)
    assert(expected.answered == actual.answered)
    assert(expected.disabled == actual.disabled)
  }

  def assertEquals(expected: GameQuestion, actual: GameQuestion): Unit = {
    assert(expected.kind == actual.kind)
    assert(expected.`type` == actual.`type`)
    assert(expected.correct == actual.correct)
    assert(expected.timeSpent == actual.timeSpent)
  }


  test("GameQuestion") {
    implicit val formats = DefaultFormats + new QuestionSerializer

    val question =
      """ {
            "type": "TLWhenDidYouShareThisPost",
            "kind": "Timeline",
            "correct": true
          }"""

    val parsedQuestion = parse(question).extract[GameQuestion]

    assert(parsedQuestion.kind == QuestionKind.Timeline)
    assert(parsedQuestion.`type` == "TLWhenDidYouShareThisPost")
    parsedQuestion.correct match {
      case Some(correct) =>
        assert(correct)
      case None =>
        fail("Correct not parsed.")
    }
  }

  test("Tile") {
    implicit val formats = DefaultFormats + new QuestionSerializer

    val tile =
      """
      {
        "_id": "87c565b56f7fc92ff8617c2b",
        "type": "Timeline",
        "question1": {
              "type": "TLWhenDidYouShareThisPost",
              "kind": "Timeline",
              "correct": true,
              "timeSpent": 4000
            },
        "question2": {
              "type": "TLWhenDidYouShareThisPost",
              "kind": "Timeline",
              "correct": true,
              "timeSpent": 4000
            },
        "question3": {
              "type": "TLWhenDidYouShareThisPost",
              "kind": "Timeline",
              "correct": true,
              "timeSpent": 4000
            },
        "score": 0,
        "answered": false,
        "disabled": false
      }"""

    val parsedTile = parse(tile).extract[Tile]
    val question = GameQuestion(QuestionKind.Timeline, "TLWhenDidYouShareThisPost", Some(true), Some(4000))
    val expectedTile = Tile("Timeline", "87c565b56f7fc92ff8617c2b", question, question, question, 0, answered = false, disabled = false)

    assertEquals(expectedTile, parsedTile)
  }

  test("Board") {
    implicit val formats = DefaultFormats + new QuestionSerializer

    val board =
      """{
          "userId": "MmYXQ5EKSgdjzP3uJ",
          "tiles": [
            {
              "_id": "79c2378123949e4386631412",
              "type": "MultipleChoice",
              "question1": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question2": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question3": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "score": 0,
              "answered": false,
              "disabled": false
            },
            {
              "_id": "9119c06edf2e473772ea37a2",
              "type": "Order",
              "question1": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question2": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question3": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "score": 0,
              "answered": false,
              "disabled": false
            },
            {
              "_id": "b7893b57c6ba4350fb3d977a",
              "type": "Order",
              "question1": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question2": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question3": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "score": 0,
              "answered": false,
              "disabled": false
            },
            {

             "_id": "6cb42b812a48c42cc4b0024c",
              "type": "MultipleChoice",
              "question1": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question2": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question3": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "score": 0,
              "answered": false,
              "disabled": false
            }
          ],
          "_id": "wQz5dAXnpDcyvrJud"
        }"""

    val actualBoard = parse(board).extract[Board]
    val question = GameQuestion(QuestionKind.Timeline, "TLWhenDidYouShareThisPost", Some(true), Some(4000))
    val expectedBoard = Board("MmYXQ5EKSgdjzP3uJ",
      List(Tile("MultipleChoice", "79c2378123949e4386631412", question, question, question, 0, answered = false, disabled = false),
        Tile("Order", "9119c06edf2e473772ea37a2", question, question, question, 0, answered = false, disabled = false),
        Tile("Order", "b7893b57c6ba4350fb3d977a", question, question, question, 0, answered = false, disabled = false),
        Tile("MultipleChoice", "6cb42b812a48c42cc4b0024c", question, question, question, 0, answered = false, disabled = false)),
      "wQz5dAXnpDcyvrJud")

    assertEquals(expectedBoard, actualBoard)
  }

  test("Game") {
    implicit val formats = DefaultFormats + new QuestionSerializer

    val game =
      """
         {
         "_id": "qS3MNajyD4qd2RYpy",
         "player1": "MmYXQ5EKSgdjzP3uJ",
         "player2": "wucQxKHvs5W9Ao9y9",
         "player1Board": {
            "userId": "MmYXQ5EKSgdjzP3uJ",
            "tiles": [
            {
              "_id": "9119c06edf2e473772ea37a2",
              "type": "Order",
              "question1": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question2": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question3": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "score": 0,
              "answered": false,
              "disabled": false
            },
            {
              "_id": "b7893b57c6ba4350fb3d977a",
              "type": "Order",
              "question1": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question2": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question3": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "score": 0,
              "answered": false,
              "disabled": false
            },
            {
              "_id": "6cb42b812a48c42cc4b0024c",
              "type": "MultipleChoice",
              "question1": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question2": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question3": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "score": 0,
              "answered": false,
              "disabled": false
            }
            ],
            "_id": "wQz5dAXnpDcyvrJud"
          },
         "player2Board": {
           "userId": "wucQxKHvs5W9Ao9y9",
           "tiles": [

             {
               "_id": "7fe68a7b1edf66b0dae1aaac",
               "type": "Geolocation",
               "question1": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question2": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question3": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
               "score": 0,
               "answered": false,
               "disabled": false
             },
             {
               "_id": "099a1cec7b072135919f04fb",
               "type": "MultipleChoice",
               "question1": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question2": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
              "question3": {
                    "type": "TLWhenDidYouShareThisPost",
                    "kind": "Timeline",
                    "correct": true,
                    "timeSpent": 4000
                  },
               "score": 0,
               "answered": false,
               "disabled": false
             }
           ],
           "_id": "fAYrgj74h9dv9Dhmq"
         },
         "status": "ended",
         "player1Score": 3,
         "player2Score": 7,
         "wonBy": 2,
         "creationTime": 1456062881638
        }"""

    val question = GameQuestion(QuestionKind.Timeline, "TLWhenDidYouShareThisPost", Some(true), Some(4000))
    val expectedGame = Game("qS3MNajyD4qd2RYpy", "MmYXQ5EKSgdjzP3uJ", "wucQxKHvs5W9Ao9y9",
      Board("MmYXQ5EKSgdjzP3uJ", List(Tile("Order", "9119c06edf2e473772ea37a2", question, question, question, 0, answered = false, disabled = false),
        Tile("Order", "b7893b57c6ba4350fb3d977a", question, question, question, 0, answered = false, disabled = false),
        Tile("MultipleChoice", "6cb42b812a48c42cc4b0024c", question, question, question, 0, answered = false, disabled = false)), "wQz5dAXnpDcyvrJud"),
      Board("wucQxKHvs5W9Ao9y9", List(Tile("Geolocation", "7fe68a7b1edf66b0dae1aaac", question, question, question, 0, answered = false, disabled = false),
        Tile("MultipleChoice", "099a1cec7b072135919f04fb", question, question, question, 0, answered = false, disabled = false)), "fAYrgj74h9dv9Dhmq"),
      "ended", 3, 7, 2, 1456062881638L)

    val actualGame = parse(game).extract[Game]

    assertEquals(expectedGame, actualGame)
  }

  test("json from file") {
    implicit val formats = DefaultFormats + new QuestionSerializer
    val stream: InputStream = getClass.getResourceAsStream("/boards.json")
    val lines = scala.io.Source.fromInputStream(stream).getLines.mkString("\n")
    val json = parse(lines)
    assert(json.extract[Game].isInstanceOf[Game])
  }

}