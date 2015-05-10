package borg.omnibus.gtfsrt

import com.google.transit.realtime.GtfsRealtime
import com.mongodb.casbah.Imports._

import scala.language.implicitConversions

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
