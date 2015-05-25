package borg.omnibus.gtfsrt

import borg.omnibus.util.MongoImplicits._
import com.google.transit.realtime.GtfsRealtime
import com.mongodb.casbah.Imports._

import scala.collection.JavaConversions._

case class TimeRange(start: Long, end: Long)
object TimeRange {
  implicit class RichTimeRange(o: TimeRange) {
    def toMongo = MongoDBObject(
      "start" -> o.start,
      "end" -> o.end
    )
  }

  def apply(mongo: MongoDBObject): TimeRange =
    TimeRange(
      mongo.as[Long]("start"),
      mongo.as[Long]("end"))

  def apply(proto: GtfsRealtime.TimeRange): TimeRange =
    TimeRange(proto.getStart, proto.getEnd)
}

case class AlertRecord(header: RecordHeader, activePeriod: Iterable[TimeRange], cause: Int, effect: Int, url: String) extends Record {
  override def toMongo = MongoDBObject(
    "header" -> header.toMongo,
    "activePeriod" -> MongoDBList.concat(activePeriod.map(_.toMongo)),
    "cause" -> cause,
    "effect" -> effect,
    "url" -> url)
}

object AlertRecord {
  def apply(header: RecordHeader, proto: GtfsRealtime.Alert): AlertRecord =
    AlertRecord(
      header,
      proto.getActivePeriodList.map(TimeRange.apply),
      proto.getCause.getNumber,
      proto.getEffect.getNumber,
      proto.getUrl.toString)

  def apply(mongo: MongoDBObject): AlertRecord =
    AlertRecord(
      RecordHeader(mongo.as[DBObject]("header")),
      mongo.objectSeq("activePeriod").map(TimeRange.apply).toList,
      mongo.as[Int]("cause"),
      mongo.as[Int]("effect"),
      mongo.as[String]("url"))

  object Codec extends Record.RecordCodec[AlertRecord](apply)
}

