package borg.omnibus.gtfs.model

import java.net.URL

import scala.language.implicitConversions

case class Stop(
  id: String,
  name: String,
  desc: String,
  lat: Double,
  lon: Double,
  zoneId: String,
  url: URL)

object Stop {
  def apply(line: String): Stop = {
    line.split(",", -1) match {
      case Array(stopId, stopName, stopDesc, lat, lon, zoneId, stopUrl) =>
        Stop(stopId, stopName, stopDesc, lat.toDouble, lon.toDouble, zoneId, new URL(stopUrl))
    }
  }
}

case class Stops(items: Map[String, Stop])

object Stops extends ModelCompanion[Stops] {
  implicit def toMap(stops: Stops): Map[String, Stop] = stops.items

  override def parse(lines: Iterable[String]): Stops = {
    val items = lines map {l =>
      val s = Stop(l)
      s.id -> s
    }
    Stops(items.toMap)
  }
}
