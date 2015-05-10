package borg.omnibus.gtfsrt

import com.google.transit.realtime.GtfsRealtime
import com.mongodb.casbah.Imports._

import scala.language.implicitConversions

case class Departure(delay: Int, uncertainty: Int)

object Departure {
  def apply(proto: GtfsRealtime.TripUpdate.StopTimeEvent): Departure = {
    Departure(proto.getDelay, proto.getUncertainty)
  }

  def apply(mongo: MongoDBObject): Departure = {
    Departure(
      mongo.as[Int]("delay"),
      mongo.as[Int]("uncertainty"))
  }

  implicit class RichDeparture(o: Departure) {
    def toMongo: MongoDBObject =
      MongoDBObject("delay" -> o.delay, "uncertainty" -> o.uncertainty)
  }
}

