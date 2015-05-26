package borg.omnibus.gtfs2

case class Trip(
  routeId: String,
  serviceId: String,
  tripId: String,
  tripHeadsign: Option[String],
  tripShortName: Option[String],
  directionId: Option[Int],
  blockId: Option[String],
  shapeId: Option[String],
  wheelchairAccessible: Option[Int],
  bikesAllowed: Option[Int])

object TripModel extends Model[Trip] {
  val RouteId = required("route_id")
  val ServiceId = required("service_id")
  val TripId = required("trip_id")
  val TripHeadsign = optional("trip_headsign")
  val TripShortName = optional("trip_short_name")
  val DirectionId = optional("direction_id")
  val BlockId = optional("block_id")
  val ShapeId = optional("shape_id")
  val WheelchairAccessible = optional("wheelchair_accessible")
  val BikesAllowed = optional("bikes_allowed")
  
  override def apply(values: Values) = 
    Trip(
      values(RouteId),
      values(ServiceId),
      values(TripId),
      values(TripHeadsign),
      values(TripShortName),
      values(DirectionId).map(_.toInt),
      values(BlockId),
      values(ShapeId),
      values(WheelchairAccessible).map(_.toInt),
      values(BikesAllowed).map(_.toInt))
}
