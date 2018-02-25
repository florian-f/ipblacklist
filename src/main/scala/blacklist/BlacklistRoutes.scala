package blacklist

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.duration._

trait BlacklistRoutes extends BlacklistJsonSupport {

  implicit def system: ActorSystem
  def blacklistActor: ActorRef
  implicit val timeout: Timeout = Timeout(3.seconds)

  import blacklist.BlackListActor._

  val routes: Route =
    pathPrefix("configurations") {
      pathEnd {
        post {
          entity(as[Configuration]) { configuration =>
            val added = (blacklistActor ? AddConfiguration(configuration)).mapTo[ConfigurationAdded]
            onSuccess(added) { created =>
              complete((StatusCodes.Created, s"Configuration for ${created.configuration.address} added."))
            }
          }
        } ~
        get {
          val blacklist = (blacklistActor ? GetBlackList).mapTo[Blacklist]
          complete(blacklist)
        }
      } ~
      path(Segment) { address =>
        get {
          val configuration = Configuration(address)
          val found = (blacklistActor ? GetConfiguration(configuration)).mapTo[Option[Configuration]]
          rejectEmptyResponse {
            complete(found)
          }
        } ~
        delete {
          val deleted = (blacklistActor ? DeleteConfiguration(Configuration(address))).mapTo[Option[Configuration]]
          rejectEmptyResponse {
            complete(deleted)
          }
        }
      }
    }
}
