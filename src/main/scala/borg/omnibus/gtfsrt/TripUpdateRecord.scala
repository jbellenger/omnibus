package borg.omnibus.gtfsrt

import borg.omnibus.gtfs.TripDirection
import borg.omnibus.util.MongoImplicits._
import com.google.transit.realtime.GtfsRealtime
import com.mongodb.casbah.Imports._

import scala.collection.JavaConversions._
import scala.concurrent.duration._
import scala.language.implicitConversions

case class TripUpdateRecord(header: RecordHeader, trip: TripDescriptor, stopTimeUpdates: Iterable[StopTimeUpdate]) extends Record {
  override def toMongo: MongoDBObject =
    MongoDBObject(
      "header" -> header.toMongo,
      "trip" -> trip.toMongo,
      "stopTimeUpdates" -> stopTimeUpdates.toMongo)
}

object TripUpdateRecord {
  def apply(header: RecordHeader, proto: GtfsRealtime.TripUpdate): TripUpdateRecord = {
    val updates = proto.getStopTimeUpdateList.toList.map(StopTimeUpdate.apply)
    TripUpdateRecord(header, TripDescriptor(proto.getTrip), updates)
  }

  def apply(mongo: MongoDBObject): TripUpdateRecord = {
    val items = mongo.objectSeq("stopTimeUpdates")
    TripUpdateRecord(
      RecordHeader(mongo.as[BasicDBObject]("header")),
      TripDescriptor(mongo.as[BasicDBObject]("trip")),
      mongo.objectSeq("stopTimeUpdates").map(StopTimeUpdate.apply))
  }

  object Codec extends Record.RecordCodec[TripUpdateRecord](apply)
}

case class StopTimeUpdate(stopSequence: Int, departure: Departure, stopId: String)

object StopTimeUpdate {
  def apply(proto: GtfsRealtime.TripUpdate.StopTimeUpdate): StopTimeUpdate = {
    val seqId = proto.getStopSequence
    StopTimeUpdate(seqId, Departure(proto.getDeparture), proto.getStopId)
  }

  def apply(mongo: MongoDBObject): StopTimeUpdate = {
    StopTimeUpdate(
      mongo.as[Int]("stopSequence"),
      Departure(mongo.as[DBObject]("departure")),
      mongo.as[String]("stopId")
    )
  }

  implicit class RichStopTimeUpdate(o: StopTimeUpdate) {
    def toMongo: MongoDBObject =
      MongoDBObject("stopSequence" -> o.stopSequence, "departure" -> o.departure.toMongo, "stopId" -> o.stopId)
  }

  implicit class RichStopTimeUpdates(os: Iterable[StopTimeUpdate]) {
    def toMongo: MongoDBList = MongoDBList.concat(os.map(_.toMongo))
  }
}

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

