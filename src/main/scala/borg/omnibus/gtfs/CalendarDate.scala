package borg.omnibus.gtfs

case class CalendarDate(
  serviceId: String,
  date: Long,
  exceptionType: Int)

object CalendarDateModel extends Model[CalendarDate] {
  val ServiceId = required("service_id")
  val Date = required("date")
  val ExceptionType = required("exception_type")

  override def apply(values: Values) =
    CalendarDate(
      values(ServiceId),
      values(Date).toLong,
      values(ExceptionType).toInt)
}

