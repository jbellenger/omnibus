package borg.omnibus.providers

import scala.concurrent.duration._

case class Provider(id: String, gtfsrt: GtfsrtApis, gtfsModels: GtfsModels)

object Providers {
  lazy val Bart = Provider(
    "bart",
    GtfsrtApis(
      15.seconds,
      TripUpdateApi("http://api.bart.gov/gtfsrt/tripupdate.aspx"),
      AlertsApi("http://api.bart.gov/gtfsrt/alerts.aspx")
    ),
    GtfsModels.fromResource("/providers/bart-gtfs.zip"))

  lazy val ArlingtonTransit = Provider(
    "art",
    GtfsrtApis(
      30.seconds,
      TripUpdateApi("http://realtime.commuterpage.com/rtt/public/utility/gtfsrealtime.aspx/tripupdate")
    ),
    GtfsModels.fromResource("/providers/art-gtfs.zip")
  )
  lazy val providers = List(Bart)
}

trait ProvidersComponent {
  def providers: Iterable[Provider]
}
