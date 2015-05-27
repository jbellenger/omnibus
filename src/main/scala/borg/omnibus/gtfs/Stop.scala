package borg.omnibus.gtfs

import java.net.URL

case class Stop(
  stopId: String,
  stopCode: Option[String],
  stopName: String,
  stopDesc: Option[String],
  stopLat: Double,
  stopLon: Double,
  zoneId: Option[String],
  stopUrl: Option[URL],
  locationType: Option[String],
  parentStation: Option[String],
  stopTimezone: Option[String],
  wheelchairBoarding: Option[Long])

object StopModel extends Model[Stop] {
  val StopId = required("stop_id")
  val StopCode = optional("stop_code")
  val StopName = required("stop_name")
  val StopDesc = optional("stop_desc")
  val StopLat = required("stop_lat")
  val StopLon = required("stop_lon")
  val ZoneId = optional("zone_id")
  val StopUrl = optional("stop_url")
  val LocationType = optional("location_type")
  val ParentStation = optional("parent_station")
  val StopTimezone = optional("stop_timezone")
  val WheelchairBoarding = optional("wheelchair_boarding")

  override def apply(values: Values): Stop = Stop(
    values(StopId),
    values(StopCode),
    values(StopName),
    values(StopDesc),
    values(StopLat).toDouble,
    values(StopLon).toDouble,
    values(ZoneId),
    values(StopUrl),
    values(LocationType),
    values(ParentStation),
    values(StopTimezone),
    values(WheelchairBoarding))
}


