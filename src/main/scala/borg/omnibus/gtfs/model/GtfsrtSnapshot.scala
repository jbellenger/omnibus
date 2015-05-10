package borg.omnibus.gtfs.model

import com.google.transit.realtime.GtfsRealtime
import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.mongodb.DBObject
import com.mongodb.casbah.commons.{MongoDBList, MongoDBObject}

import scala.collection.JavaConversions._
import scala.language.implicitConversions

case class Departure(delay: Int, uncertainty: Int)
object Departure {
  def apply(proto: GtfsRealtime.TripUpdate.StopTimeEvent): Departure = {
    Departure(proto.getDelay, proto.getUncertainty)
  }

  implicit class RichDeparture(o: Departure) {
    def toMongo: DBObject =
      MongoDBObject("delay" -> o.delay, "uncertainty" -> o.uncertainty)
  }
}

case class StopTimeUpdate(stopSequence: Int, departure: Departure, stopId: String)
object StopTimeUpdate {
  def apply(proto: GtfsRealtime.TripUpdate.StopTimeUpdate): StopTimeUpdate = {
    val seqId = proto.getStopSequence
    StopTimeUpdate(seqId, Departure(proto.getDeparture), proto.getStopId)
  }

  implicit class RichStopTimeUpdate(o: StopTimeUpdate) {
    def toMongo: DBObject =
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

  implicit class RichTripDescriptor(o: TripDescriptor) {
    def toMongo: DBObject =
      MongoDBObject("tripId" -> o.tripId, "direction" -> o.direction.map(_.toInt), "routeId" -> o.routeId)
  }
}

case class TripUpdate(trip: TripDescriptor, stopTimeUpdates: Iterable[StopTimeUpdate])
object TripUpdate {
  def apply(proto: GtfsRealtime.TripUpdate): TripUpdate = {
    val updates = proto.getStopTimeUpdateList.toList.map(StopTimeUpdate.apply)
    TripUpdate(TripDescriptor(proto.getTrip), updates)
  }

  implicit class RichTripUpdate(o: TripUpdate) {
    def toMongo: DBObject =
      MongoDBObject("trip" -> o.trip.toMongo, "stopTimeUpdates" -> o.stopTimeUpdates.toMongo)
  }
}

case class SnapshotHeader(stamp: Long)
object SnapshotHeader {
  def apply(proto: GtfsRealtime.FeedHeader): SnapshotHeader = {
    require(proto.getGtfsRealtimeVersion == "1.0", s"unknown version: ${proto.getGtfsRealtimeVersion}")
    require(proto.getIncrementality == GtfsRealtime.FeedHeader.Incrementality.FULL_DATASET)
    SnapshotHeader(proto.getTimestamp)
  }

  implicit class RichSnapshotHeader(o: SnapshotHeader) {
    def toMongo: DBObject = MongoDBObject("stamp" -> o.stamp)
  }
}

case class GtfsrtSnapshot(header: SnapshotHeader, items: Iterable[TripUpdate])

object GtfsrtSnapshot {
  def apply(msg: FeedMessage): GtfsrtSnapshot = {
    val head = SnapshotHeader(msg.getHeader)
    val items = msg.getEntityList.toList.map(_.getTripUpdate).map(TripUpdate.apply)
    GtfsrtSnapshot(head, items)
  }

  implicit class RichGtfsrtSnapshot(o: GtfsrtSnapshot) {
    def toMongo: DBObject =
      MongoDBObject("header" -> o.header.toMongo, "items" -> MongoDBList.concat(o.items.map(_.toMongo)))
  }
}
