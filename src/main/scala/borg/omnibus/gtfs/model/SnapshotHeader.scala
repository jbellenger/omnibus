package borg.omnibus.gtfs.model

import com.google.transit.realtime.GtfsRealtime
import com.mongodb.casbah.Imports._

import scala.language.implicitConversions

case class SnapshotHeader(stamp: Long)

object SnapshotHeader {
  def apply(proto: GtfsRealtime.FeedHeader): SnapshotHeader = {
    require(proto.getGtfsRealtimeVersion == "1.0", s"unknown version: ${proto.getGtfsRealtimeVersion}")
    require(proto.getIncrementality == GtfsRealtime.FeedHeader.Incrementality.FULL_DATASET)
    SnapshotHeader(proto.getTimestamp)
  }

  def apply(o: MongoDBObject): SnapshotHeader =
    SnapshotHeader(o.as[Long]("stamp"))

  implicit class RichSnapshotHeader(o: SnapshotHeader) {
    def toMongo: MongoDBObject = MongoDBObject("stamp" -> o.stamp)
  }
}
