package borg.omnibus.gtfs.model

import com.google.transit.realtime.GtfsRealtime
import com.mongodb.casbah.Imports._

import scala.language.implicitConversions

case class TripDescriptor(tripId: String, direction: Option[TripDirection], routeId: String)

object TripDescriptor {
  def apply(proto: GtfsRealtime.TripDescriptor): TripDescriptor = {
    val direction =
      if (proto.hasDirectionId) Some(TripDirection(proto.getDirectionId))
      else None
    TripDescriptor(proto.getTripId, direction, proto.getRouteId)
  }

  def apply(mongo: MongoDBObject): TripDescriptor = {
    TripDescriptor(
      mongo.as[String]("tripId"),
      mongo.getAs[Int]("direction").map(TripDirection.apply),
      mongo.as[String]("routeId"))
  }

  implicit class RichTripDescriptor(o: TripDescriptor) {
    def toMongo: MongoDBObject =
      MongoDBObject("tripId" -> o.tripId, "direction" -> o.direction.map(_.toInt), "routeId" -> o.routeId)
  }
}


