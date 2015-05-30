package borg.omnibus.gtfsrt

import borg.omnibus.providers.ProviderId
import com.google.transit.realtime.GtfsRealtime
import com.mongodb.casbah.Imports._

import scala.language.implicitConversions

case class RecordHeader(providerId: String, stamp: Long)

object RecordHeader {
  def apply(id: ProviderId, proto: GtfsRealtime.FeedHeader): RecordHeader = {
    require(proto.getGtfsRealtimeVersion == "1.0", s"unknown version: ${proto.getGtfsRealtimeVersion}")
    require(proto.getIncrementality == GtfsRealtime.FeedHeader.Incrementality.FULL_DATASET)
    RecordHeader(id.id, proto.getTimestamp)
  }

  def apply(o: MongoDBObject): RecordHeader =
    RecordHeader(
      o.as[String]("pid"),
      o.as[Long]("stamp"))

  implicit class RichSnapshotHeader(o: RecordHeader) {
    def toMongo: MongoDBObject =
      MongoDBObject("pid" -> o.providerId, "stamp" -> o.stamp)
  }
}
