package borg.omnibus.gtfs.model

import java.net.URL

import scala.language.implicitConversions

case class Route(
  routeId: String,
  agencyId: String,
  routeShortName: String,
  routeLongName: String,
  routeDesc: String,
  routeType: String,
  routeUrl: URL,
  routeColor: String,
  routeTextColor: String)

object Route extends {
  def apply(line: String): Route = {
    line.split(",", -1) match {
      case arr: Array[String] if arr.length == 9 =>
        val a = arr.asInstanceOf[Array[String]]
        Route(
          a(0),
          a(1),
          a(2),
          a(3),
          a(4),
          a(5),
          new URL(a(6)),
          a(7),
          a(8)
        )

    }
  }
}

case class Routes(items: Map[String, Route])

object Routes extends ModelCompanion[Routes] {
  implicit def toMap(routes: Routes): Map[String, Route] = routes.items

  override def parse(lines: Iterable[String]): Routes =
    Routes(toMap[Route](lines, Route.apply, _.routeId))
}
