package borg.omnibus.gtfs

import java.net.URL

import scala.io.Source
import scala.reflect.io.ZipArchive

trait GtfsDatabase {
  val agencies: Seq[Agency]
  val stops: Map[String, Stop]
  val routes: Map[String, Route]
  val trips: Map[String, Trip]
  val stopTimes: Seq[StopTime]
  val calendar: Seq[Calendar]
  val calendarDates: Seq[CalendarDate]
  val fareAttributes: Seq[FareAttribute]
  val fareRules: Seq[FareRule]
  val shapes: Map[String, Shape]
  val frequencies: Map[String, Frequency]
  val transfers: Seq[Transfer]
  val feedInfo: Seq[FeedInfo]
}

object GtfsDatabase {
  def fromResource(resourcePath: String): GtfsDatabase = {
    fromUrl(getClass.getResource(resourcePath))
  }

  def fromUrl(url: URL): GtfsDatabase = {
    val zip = ZipArchive.fromURL(url)
    val files = zip.map { file =>
      file.canonicalPath -> Source.fromBytes(file.toByteArray)
    }.toMap

    new GtfsDatabase {
      // required
      override val agencies =
        read(files("agency.txt"), AgencyModel)
      override val stops =
        toMap[String, Stop](read(files("stops.txt"), StopModel), _.stopId)
      override val routes =
        toMap[String, Route](read(files("routes.txt"), RouteModel), _.routeId)
      override val trips =
        toMap[String, Trip](read(files("trips.txt"), TripModel), _.tripId)
      override val stopTimes =
        read(files("stop_times.txt"), StopTimeModel)
      override val calendar =
        read(files("calendar.txt"), CalendarModel)

      // optional
      override val calendarDates =
        read(files.get("calendar_dates.txt"), CalendarDateModel)
      override val fareRules =
        read(files.get("fare_rules.txt"), FareRuleModel)
      override val fareAttributes =
        read(files.get("fare_attributes.txt"), FareAttributeModel)
      override val transfers =
        read(files.get("transfers.txt"), TransferModel)
      override val feedInfo =
        read(files.get("feed_info.txt"), FeedInfoModel)
      override val shapes =
        toMap[String, Shape](read(files.get("shapes.txt"), ShapeModel), _.shapeId)
      override val frequencies =
        toMap[String, Frequency](read(files.get("frequencies.txt"), FrequencyModel), _.tripId)
    }
  }

  private def read[T](source: Source, model: Model[T]): Stream[T] = {
    val lines = source.getLines().toStream
    val reader = new Reader(model, lines.head)
    lines.tail.map(_.trim()).filter(_.nonEmpty).map(reader.map)
  }

  private def read[T](source: Option[Source], model: Model[T]): Stream[T] = {
    source match {
      case Some(s) => read(s, model)
      case _ => Stream.empty[T]
    }
  }

  private def toMap[K, V](vs: Iterable[V], fn: V => K): Map[K, V] = {
    vs.map {v =>
      fn(v) -> v
    }.toMap
  }
}

object Test {
  def main(args: Array[String]): Unit = {
    GtfsDatabase.fromResource("/providers/bart-gtfs.zip")
  }
}
