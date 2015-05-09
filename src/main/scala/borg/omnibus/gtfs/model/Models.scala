package borg.omnibus.gtfs.model

import java.net.URL

import scala.io.Source
import scala.reflect.io.ZipArchive

case class Models(
  stops: Stops,
  trips: Trips,
  routes: Routes,
  stopTimes: StopTimes
)

object Models {
  def fromResource(resourcePath: String): Models =
    fromUrl(getClass.getResource(resourcePath))

  def fromUrl(url: URL): Models = {
    val zip = ZipArchive.fromURL(url)
    val data = zip.map { file =>
      val body = Source.fromBytes(file.toByteArray)
      file.canonicalPath -> body.mkString
    }.toMap
    apply(data)
  }

  private def fromCsv[T](body: String, companion: ModelCompanion[T]): T = {
    // skip first line, header
    body.lines.toList match {
      case header :: body =>
        companion.parse(body)
    }
  }

  def apply(data: Map[String, String]): Models = {
    Models(
      fromCsv(data("stops.txt"), Stops),
      fromCsv(data("trips.txt"), Trips),
      fromCsv(data("routes.txt"), Routes),
      fromCsv(data("stop_times.txt"), StopTimes)
    )
  }
}
