package borg.omnibus.gtfsrt

import com.google.transit.realtime.GtfsRealtime
import com.mongodb.casbah.Imports._

case class VehiclePositionRecord(header: RecordHeader, position: Position, stopSeq: Int, stopId: String, stamp: Long) extends Record {
  override def toMongo = MongoDBObject(
    "header" -> header.toMongo,
    "stopSeq" -> stopSeq,
    "stopId" -> stopId,
    "stamp" -> stamp,
    "pos" -> position.toMongo
  )
}

object VehiclePositionRecord {
  def apply(header: RecordHeader, proto: GtfsRealtime.VehiclePosition): VehiclePositionRecord =
    VehiclePositionRecord(
      header,
      Position(proto.getPosition),
      proto.getCurrentStopSequence,
      proto.getStopId,
      proto.getTimestamp)

  def apply(mongo: MongoDBObject): VehiclePositionRecord =
    VehiclePositionRecord(
      RecordHeader(mongo.as[BasicDBObject]("header")),
      Position(mongo.as[BasicDBObject]("pos")),
      mongo.as[Int]("stopSeq"),
      mongo.as[String]("stopId"),
      mongo.as[Long]("stamp")
    )

  object Codec extends Record.RecordCodec[VehiclePositionRecord](apply)
}

case class Position(lat: Float, lon: Float, bearing: Option[Float], speed: Option[Float])

object Position {
  implicit class RichPosition(o: Position) {
    def toMongo: MongoDBObject = MongoDBObject(
      "lat" -> o.lat,
      "lon" -> o.lon,
      "bearing" -> o.bearing,
      "speed" -> o.speed
    )
  }

  def apply(proto: GtfsRealtime.Position): Position =
    Position(
      proto.getLatitude,
      proto.getLongitude,
      Option(proto.getBearing).filter(_ => proto.hasBearing),
      Option(proto.getSpeed).filter(_ => proto.hasSpeed))

  def apply(mongo: MongoDBObject): Position =
    Position(
      mongo.as[Float]("lat"),
      mongo.as[Float]("lon"),
      mongo.getAs[Float]("bearing"),
      mongo.getAs[Float]("speed"))
}
