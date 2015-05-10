package borg.omnibus.gtfs.model

import java.net.URL

import scala.io.Source
import scala.reflect.io.ZipArchive

case class GtfsModels(
  stops: Stops,
  trips: Trips,
  routes: Routes,
  stopTimes: StopTimes
)

object GtfsModels {
  def fromResource(resourcePath: String): GtfsModels =
    fromUrl(getClass.getResource(resourcePath))

  def fromUrl(url: URL): GtfsModels = {
    val zip = ZipArchive.fromURL(url)
    val data = zip.map { file =>
      val body = Source.fromBytes(file.toByteArray)
      file.canonicalPath -> body.mkString
    }.toMap
    apply(data)
  }

  private def fromCsv[T](body: String, companion: ModelCompanion[T]): T = {
    body.lines.toList match {
      case header :: tail =>
        companion.parse(tail)
      case Nil => throw new IllegalArgumentException("body is empty")
    }
  }

  def apply(data: Map[String, String]): GtfsModels = {
    GtfsModels(
      fromCsv(data("stops.txt"), Stops),
      fromCsv(data("trips.txt"), Trips),
      fromCsv(data("routes.txt"), Routes),
      fromCsv(data("stop_times.txt"), StopTimes)
    )
  }
}
