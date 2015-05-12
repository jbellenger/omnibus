package borg.omnibus.gtfsrt

import com.google.transit.realtime.GtfsRealtime
import com.mongodb.casbah.Imports._

import scala.concurrent.duration.{FiniteDuration, _}
import scala.language.implicitConversions

case class Departure(delay: FiniteDuration, uncertainty: Int)

object Departure {
  def apply(proto: GtfsRealtime.TripUpdate.StopTimeEvent): Departure = {
    Departure(proto.getDelay.seconds, proto.getUncertainty)
  }

  def apply(mongo: MongoDBObject): Departure = {
    Departure(
      mongo.as[Long]("delay").seconds,
      mongo.as[Int]("uncertainty"))
  }

  implicit class RichDeparture(o: Departure) {
    def toMongo: MongoDBObject =
      MongoDBObject("delay" -> o.delay.toSeconds, "uncertainty" -> o.uncertainty)
  }
}

