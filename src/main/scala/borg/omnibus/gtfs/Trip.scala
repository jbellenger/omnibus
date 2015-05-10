package borg.omnibus.gtfs

import scala.language.implicitConversions

case class Trip(routeId: String, serviceId: String, tripId: String, tripHeadSign: String, direction: TripDirection, blockId: String, shapeId: String)

object Trip {
  def apply(string: String): Trip = {
    string.split(",", -1) match {
      case Array(routeId, serviceId, tripId, tripHeadSign, TripDirection(dirId), blockId, shapeId) =>
        Trip(routeId, serviceId, tripId, tripHeadSign, dirId, blockId, shapeId)
    }
  }
}

case class Trips(items: Map[String, Trip])

object Trips extends ModelCompanion[Trips] {
  implicit def toMap(trips: Trips): Map[String, Trip] = trips.items

  override def parse(lines: Iterable[String]): Trips =
    Trips(toMap[Trip](lines, Trip.apply, _.routeId))
}