package blacklist

import akka.actor.{ActorRef, ActorSystem, Scheduler}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration._

object Api extends App with BlacklistRoutes {

  override implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val sched: Scheduler = system.scheduler

  val log: LoggingAdapter = Logging(system, getClass)

  override val blacklistActor: ActorRef = system.actorOf(BlackListActor.props)

  val bindingFuture = Http().bindAndHandle(routes, "localhost", 9090)

  log.debug(s"Server online at http://localhost:9090/")

  scala.sys.addShutdownHook {
    log.info("Shutting down.")
    system.terminate()
    Await.result(system.whenTerminated, 30 seconds)
    log.info("Shut down.")
  }
}