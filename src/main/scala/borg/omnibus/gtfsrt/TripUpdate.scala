package borg.omnibus.gtfsrt

import borg.omnibus.util.MongoImplicits._
import com.google.transit.realtime.GtfsRealtime
import com.mongodb.casbah.Imports._

import scala.collection.JavaConversions._
import scala.language.implicitConversions

case class TripUpdate(trip: TripDescriptor, stopTimeUpdates: Iterable[StopTimeUpdate])

object TripUpdate {
  def apply(proto: GtfsRealtime.TripUpdate): TripUpdate = {
    val updates = proto.getStopTimeUpdateList.toList.map(StopTimeUpdate.apply)
    TripUpdate(TripDescriptor(proto.getTrip), updates)
  }

  def apply(mongo: MongoDBObject): TripUpdate = {
    val items = mongo.objectSeq("stopTimeUpdates")
    TripUpdate(
      TripDescriptor(mongo.as[BasicDBObject]("trip")),
      mongo.objectSeq("stopTimeUpdates").map(StopTimeUpdate.apply))
  }

  implicit class RichTripUpdate(o: TripUpdate) {
    def toMongo: MongoDBObject =
      MongoDBObject("trip" -> o.trip.toMongo, "stopTimeUpdates" -> o.stopTimeUpdates.toMongo)
  }
}

