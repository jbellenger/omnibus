package borg.omnibus.gtfsrt

import com.google.transit.realtime.GtfsRealtime
import com.mongodb.casbah.Imports._

case class VehiclePositionRecord(header: RecordHeader) extends Record {
  override def toMongo = MongoDBObject(
    "header" -> header.toMongo
  )
}

object VehiclePositionRecord {
  def apply(header: RecordHeader, proto: GtfsRealtime.VehiclePosition): VehiclePositionRecord =
    VehiclePositionRecord(header)

  def apply(mongo: MongoDBObject): VehiclePositionRecord =
    VehiclePositionRecord(
      RecordHeader(mongo.as[BasicDBObject]("header"))
    )

  object Codec extends Record.RecordCodec[VehiclePositionRecord](apply)
}
