package blacklist

import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{MessageEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

class BlacklistRouteSpec extends WordSpec with Matchers with ScalatestRouteTest with ScalaFutures with BlacklistRoutes  {

  override val blacklistActor: ActorRef = system.actorOf(BlackListActor.props)

  "BlacklistRoutes" should {
    "respond with empty list when no configurations are present" in {
      Get("/configurations") ~> routes ~> check {
        responseAs[Blacklist] shouldBe Blacklist(Nil)
      }
    }
    "create configuration" in {
      val config = Configuration("192.168.0.1")
      val configEntity = Marshal(config).to[MessageEntity].futureValue

      Post("/configurations").withEntity(configEntity) ~> routes ~> check {
        status shouldBe StatusCodes.Created
      }
    }
    "get blacklist" in {
      Get("/configurations") ~> routes ~> check {
        responseAs[Blacklist] shouldBe Blacklist(Seq(Configuration("192.168.0.1")))
      }
    }
    "delete entry" in {
      Delete("/configurations/192.168.0.1") ~> routes ~> check {
        status shouldBe StatusCodes.OK
      }
    }
  }
}
