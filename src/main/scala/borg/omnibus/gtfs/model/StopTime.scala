package borg.omnibus.gtfs.model

case class StopTime(
  tripId: String,
  arrivalTime: String,      // bart uses a 25-hour clock, which makes parsing difficult
  departureTime: String,
  stopId: String,
  stopSequence: Int,
  stopHeadSign: String,
  pickupType: String,
  dropOffType: String)

object StopTime {
  def apply(line: String): StopTime = {
    line.split(",", -1) match {
      case Array(tripId, arrivalTime, departureTime, stopId, stopSeq, stopHeadSign, pickupType, dropOffType) =>
        StopTime(tripId, arrivalTime, departureTime, stopId, stopSeq.toInt, stopHeadSign, pickupType, dropOffType)
    }
  }
}

case class StopTimes(items: Map[String, Seq[StopTime]])

object StopTimes extends ModelCompanion[StopTimes] {
  override def parse(lines: Iterable[String]): StopTimes = {
    val items = lines
      .map(StopTime.apply)
      .groupBy(_.tripId)
      .mapValues {
        case xs => xs.toList.sortBy(_.stopSequence)
      }
    StopTimes(items)
  }
}
