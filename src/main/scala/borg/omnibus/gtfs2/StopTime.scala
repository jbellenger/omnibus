package borg.omnibus.gtfs2

case class StopTime(
  tripId: String,
  arrivalTime: String,
  departureTime: String,
  stopId: String,
  stopSequence: Int,
  stopHeadsign: Option[String],
  pickupType: Option[Int],
  dropOffType: Option[Int],
  shapeDistTraveled: Option[String],
  timepoint: Option[Int])

object StopTimeModel extends Model[StopTime] {
  val TripId = required("trip_id")
  val ArrivalTime = required("arrival_time")
  val DepartureTime = required("departure_time")
  val StopId = required("stop_id")
  val StopSequence = required("stop_sequence")
  val StopHeadsign = optional("stop_headsign")
  val PickupType = optional("pickup_type")
  val DropOffType = optional("drop_off_type")
  val ShapeDistTraveled = optional("shape_dist_traveled")
  val Timepoint = optional("timepoint")

  override def apply(values: Values) =
    StopTime(
      values(TripId),
      values(ArrivalTime),
      values(DepartureTime),
      values(StopId),
      values(StopSequence).toInt,
      values(StopHeadsign),
      values(PickupType).map(_.toInt),
      values(DropOffType).map(_.toInt),
      values(ShapeDistTraveled),
      values(Timepoint).map(_.toInt))
}
