package borg.omnibus.gtfs2

import java.net.URL

case class Route(
  routeId: String,
  agencyId: Option[String],
  routeShortName: String,
  routeLongName: String,
  routeDesc: Option[String],
  routeType: Long,
  routeUrl: Option[URL],
  routeColor: Option[String],
  routeTextColor: Option[String])

object RouteModel extends Model[Route] {
  val RouteId = required("route_id")
  val AgencyId = optional("agency_id")
  val RouteShortName = required("route_short_name")
  val RouteLongName = required("route_long_name")
  val RouteDesc = optional("route_desc")
  val RouteType = required("route_type")
  val RouteUrl = optional("route_url")
  val RouteColor = optional("route_color")
  val RouteTextColor = optional("route_text_color")

  override def apply(values: Values) =
    Route(
      values(RouteId),
      values(AgencyId),
      values(RouteShortName),
      values(RouteLongName),
      values(RouteDesc),
      values(RouteType).toLong,
      values(RouteUrl),
      values(RouteColor),
      values(RouteTextColor))
}
