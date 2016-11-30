package me.reminisce.stats.server

import java.util.concurrent.TimeUnit

import akka.testkit.TestActorRef
import me.reminisce.testutils.database._
import org.json4s.JsonAST.{JObject, JString}
import org.json4s.jackson.JsonMethods._
import org.json4s.{DefaultFormats, Formats, _}
import reactivemongo.api.MongoDriver
import spray.http.ContentTypes._
import spray.http._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.util.Random

class StatsServerActorSpec extends DatabaseTester("StatsServerActorSpec") {

  val port = DatabaseTestHelper.port
  val dbId = Random.nextInt
  val mongoHost = s"localhost:$port"
  val dbName = s"DB${dbId}_for_StatsServerActorSpec"
  val testService = TestActorRef(new StatsServerActor(mongoHost, dbName))
  val randomID: String = java.util.UUID.randomUUID.toString
  val randomUser1: String = java.util.UUID.randomUUID.toString
  val randomUser2: String = java.util.UUID.randomUUID.toString
  val urlInsert = "/insertEntity"

  implicit def json4sFormats: Formats = DefaultFormats + new QuestionSerializer

  case class SimpleMessageFormat(message: String)

  override def afterAll(): Unit = {
    val driver = new MongoDriver
    val connection = driver.connection(List(mongoHost))
    whenReady(connection.database(dbName)) {
      db =>
        db.drop()
    }
    super.afterAll()
  }

  "ServerServiceActor" must {

    "try to insert a Game." in {

      val gameJson: String = JsonEntity.game(randomID, randomUser1, randomUser2)
      val postRequest = HttpRequest(
        method = HttpMethods.POST,
        uri = urlInsert,
        entity = HttpEntity(`application/json`, gameJson)
      )

      assert(postRequest.method == HttpMethods.POST)
      testService ! postRequest

      val responseOpt = Option(receiveOne(Duration(10, TimeUnit.SECONDS)))

      responseOpt match {
        case Some(response) =>
          assert(response.isInstanceOf[HttpResponse])

          val httpResponse = response.asInstanceOf[HttpResponse]
          val json = parse(httpResponse.entity.data.asString)
          json match {
            case JObject(List((k, msg))) =>
              assert(k == "status")
              assert(msg == JString("Done"))
            case _ => fail("Fail to insert")
          }
        case None =>
          fail("Response is not defined.")
      }
    }

    "try to retrieve Stats for an unknown user." in {
      val getRequest = HttpRequest(uri = s"/stats?userId=NOT$randomUser1")
      assert(getRequest.method == HttpMethods.GET)
      testService ! getRequest
      val responseOpt = Option(receiveOne(Duration(10, TimeUnit.SECONDS)))
      responseOpt match {
        case Some(response) =>
          assert(response.isInstanceOf[HttpResponse])
          val httpResponse = response.asInstanceOf[HttpResponse]
          val json = parse(httpResponse.entity.data.asString)
          json match {
            case JObject(List((k, msg))) =>
              assert(k == "message")
              assert(msg == JString(s"Statistics not found for NOT$randomUser1"))
            case _ => fail("Response is not defined.")
          }
        case None =>
          fail("No response")
      }
    }

    "try to retrieve Stats for an existing user." in {
      val getRequest = HttpRequest(uri = s"/stats?userId=$randomUser1")
      assert(getRequest.method == HttpMethods.GET)
      testService ! getRequest
      val responseOpt = Option(receiveOne(Duration(10, TimeUnit.SECONDS)))
      responseOpt match {
        case Some(response) =>
          assert(response.isInstanceOf[HttpResponse])
          val httpResponse = response.asInstanceOf[HttpResponse]
          val json = parse(httpResponse.entity.data.asString)

          json match {
            case JObject(a) =>
              a match {
                case (s, _) :: tail =>
                  assert(s == "stats")
                case _ =>
                  fail("Unexpected response")
              }
              
            case _ =>
              fail("Response isn't a Stats object")
          }
        //TODO create deserializer for Stats
        case None =>
          fail("Response is not defined.")
      }
    }
    "try to insert a duplicate game" in {
      val gameJson: String = JsonEntity.game(randomID, randomUser1, randomUser2)

      val postRequest = HttpRequest(
        method = HttpMethods.POST,
        uri = urlInsert,
        entity = HttpEntity(`application/json`, gameJson)
      )
      assert(postRequest.method == HttpMethods.POST)
      testService ! postRequest

      val responseOpt = Option(receiveOne(Duration(10, TimeUnit.SECONDS)))

      responseOpt match {
        case Some(response) =>
          assert(response.isInstanceOf[HttpResponse])
          val httpResponse = response.asInstanceOf[HttpResponse]
          val json = parse(httpResponse.entity.data.asString)
          json match {
            case JObject(a) =>
              a match {
                case (x, y) :: Nil =>
                  assert((x, y) == ("status", JString("Aborted")))
                case _ =>
                  fail("Response doesn't match the exected 'aborted' message")
              }
            case _ => fail("Fail to insert")
          }
        case None =>
          fail("Response is not defined.")
      }
    }
  }
}