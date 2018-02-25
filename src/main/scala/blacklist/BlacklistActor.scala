package blacklist

import akka.actor.{Actor, ActorLogging, Props}
import com.google.common.net.InetAddresses

final case class Configuration(address: String) {
  require(InetAddresses.isInetAddress(address))
}

final case class Blacklist(configurations: Seq[Configuration])

class BlackListActor extends Actor with ActorLogging {
  import BlackListActor._

  var blacklist = Set.empty[Configuration]

  def receive: Receive = {
    case GetBlackList =>
      sender() ! Blacklist(blacklist.toSeq)
    case AddConfiguration(configuration) =>
      blacklist += configuration
      sender() ! ConfigurationAdded(configuration)
    case GetConfiguration(Configuration(address)) =>
      sender() ! blacklist.find(_.address == address)
    case DeleteConfiguration(Configuration(address)) =>
      val config = blacklist.find(_.address == address)
      config match {
        case Some(c) => blacklist -= c
        case None =>
      }
      sender() ! config
  }
}
object BlackListActor {
  final case object GetBlackList
  final case class GetConfiguration(configuration: Configuration)
  final case class AddConfiguration(configuration: Configuration)
  final case class DeleteConfiguration(configuration: Configuration)

  final case class ConfigurationDeleted(configuration: Configuration)
  final case class ConfigurationAdded(configuration: Configuration)

  def props: Props = Props[BlackListActor]
}