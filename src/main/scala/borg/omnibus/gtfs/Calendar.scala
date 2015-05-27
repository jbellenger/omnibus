package borg.omnibus.gtfs

case class Calendar(
  serviceId: String,
  monday: Int,
  tuesday: Int,
  wednesday: Int,
  thursday: Int,
  friday: Int,
  saturday: Int,
  sunday: Int,
  startDate: Long,
  endDate: Long)

object CalendarModel extends Model[Calendar] {
  val ServiceId = required("service_id")
  val Monday = required("monday")
  val Tuesday = required("tuesday")
  val Wednesday = required("wednesday")
  val Thursday = required("thursday")
  val Friday = required("friday")
  val Saturday = required("saturday")
  val Sunday = required("sunday")
  val StartDate = required("start_date")
  val EndDate = required("end_date")

  override def apply(values: Values) =
    Calendar(
      values(ServiceId),
      values(Monday).toInt,
      values(Tuesday).toInt,
      values(Wednesday).toInt,
      values(Thursday).toInt,
      values(Friday).toInt,
      values(Saturday).toInt,
      values(Sunday).toInt,
      values(StartDate).toLong,
      values(EndDate).toLong)
}

