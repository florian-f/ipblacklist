package blacklist

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait BlacklistJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val configurationFormat: RootJsonFormat[Configuration] = jsonFormat1(Configuration)
  implicit val blacklistFormat: RootJsonFormat[Blacklist] = jsonFormat1(Blacklist)
}
