package borg.omnibus.gtfs.model

import borg.omnibus.gtfs.model.MongoImplicits._
import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.mongodb.casbah.Imports._

import scala.collection.JavaConversions._
import scala.language.implicitConversions

case class GtfsrtSnapshot(header: SnapshotHeader, items: Iterable[TripUpdate])

object GtfsrtSnapshot {
  def apply(msg: FeedMessage): GtfsrtSnapshot = {
    val head = SnapshotHeader(msg.getHeader)
    val items = msg.getEntityList.toList.map(_.getTripUpdate).map(TripUpdate.apply)
    GtfsrtSnapshot(head, items)
  }

  def apply(mongo: MongoDBObject): GtfsrtSnapshot = {
    GtfsrtSnapshot(
      SnapshotHeader(mongo.as[DBObject]("header")),
      mongo.objectSeq("items").map(TripUpdate.apply))
  }

  implicit class RichGtfsrtSnapshot(o: GtfsrtSnapshot) {
    def toMongo: MongoDBObject =
      MongoDBObject("header" -> o.header.toMongo, "items" -> MongoDBList.concat(o.items.map(_.toMongo)))
  }
}
