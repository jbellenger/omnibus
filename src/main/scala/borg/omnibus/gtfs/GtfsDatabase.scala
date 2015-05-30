package borg.omnibus.gtfs

import java.net.URL
import java.util.zip.ZipFile

import borg.omnibus.csv.Reader
import borg.omnibus.util.PrintTiming

import scala.collection.JavaConversions._
import scala.io.Source

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

object GtfsDatabase extends PrintTiming {
  def fromResource(resourcePath: String): GtfsDatabase = {
    val url = getClass.getResource(resourcePath)
    fromUrl(url)
  }

  def fromUrl(url: URL): GtfsDatabase = {
    val jfile = new java.io.File(url.toURI)
    val zip = new ZipFile(jfile)
    fromZip(zip)
  }

  def fromZip(zip: ZipFile): GtfsDatabase = {
    val files = zip.entries().map {entry =>
      val stream = zip.getInputStream(entry)
      entry.getName -> Source.fromInputStream(stream).mkString
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

  private def read[T](body: String, model: Model[T]): Seq[T] = {
    Reader(body, model).toStream
  }

  private def read[T](body: Option[String], model: Model[T]): Seq[T] = {
    body match {
      case Some(b) => read(b, model)
      case _ => Stream.empty[T]
    }
  }

  private def toMap[K, V](vs: Iterable[V], fn: V => K): Map[K, V] = {
    vs.map {v =>
      fn(v) -> v
    }.toMap
  }
}
