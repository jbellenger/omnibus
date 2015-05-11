package borg.omnibus.gtfsrt

import borg.omnibus.providers.{Provider, ProviderId}
import borg.omnibus.store.Codec
import borg.omnibus.util.MongoImplicits._
import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.mongodb.casbah.Imports._

import scala.collection.JavaConversions._
import scala.language.implicitConversions

case class GtfsrtSnapshot(providerId: ProviderId, header: SnapshotHeader, items: Iterable[TripUpdate])

object GtfsrtSnapshot {
  def apply(provider: Provider, msg: FeedMessage): GtfsrtSnapshot = {
    val head = SnapshotHeader(msg.getHeader)
    val items = msg.getEntityList.toList.map(_.getTripUpdate).map(TripUpdate.apply)
    GtfsrtSnapshot(provider.id, head, items)
  }

  def apply(mongo: MongoDBObject): GtfsrtSnapshot = {
    GtfsrtSnapshot(
      ProviderId(mongo.as[String]("provider")),
      SnapshotHeader(mongo.as[DBObject]("header")),
      mongo.objectSeq("items").map(TripUpdate.apply))
  }

  implicit class RichGtfsrtSnapshot(o: GtfsrtSnapshot) {
    def toMongo: MongoDBObject =
      MongoDBObject(
        "provider" -> o.providerId.name,
        "header" -> o.header.toMongo,
        "items" -> MongoDBList.concat(o.items.map(_.toMongo)))
  }

  object MongoCodec extends Codec[GtfsrtSnapshot, MongoDBObject] {
    override def encode(a: GtfsrtSnapshot): MongoDBObject = a.toMongo
    override def decode(b: MongoDBObject): GtfsrtSnapshot = GtfsrtSnapshot(b)
  }
}
