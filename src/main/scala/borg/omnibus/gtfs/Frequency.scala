package borg.omnibus.gtfs

case class Frequency(
  tripId: String,
  startTime: Long,
  endTime: Long,
  headwaySecs: Int,
  exactTimes: Option[Boolean])

object FrequencyModel extends Model[Frequency] {
  val TripId = required("trip_id")
  val StartTime = required("start_time")
  val EndTime = required("end_time")
  val HeadwaySecs = required("headway_secs")
  val ExactTimes = optional("exact_times")

  override def apply(values: Values) =
    Frequency(
      values(TripId),
      values(StartTime).toLong,
      values(EndTime).toLong,
      values(HeadwaySecs).toInt,
      values(ExactTimes).map(_.toBoolean))
}
