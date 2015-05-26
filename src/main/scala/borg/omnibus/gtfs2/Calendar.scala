package borg.omnibus.gtfs2

case class Calendar(
  serviceId: String,
  monday: Boolean,
  tuesday: Boolean,
  wednesday: Boolean,
  thursday: Boolean,
  friday: Boolean,
  saturday: Boolean,
  sunday: Boolean,
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
      values(Monday).toBoolean,
      values(Tuesday).toBoolean,
      values(Wednesday).toBoolean,
      values(Thursday).toBoolean,
      values(Friday).toBoolean,
      values(Saturday).toBoolean,
      values(Sunday).toBoolean,
      values(StartDate).toLong,
      values(EndDate).toLong)
}

