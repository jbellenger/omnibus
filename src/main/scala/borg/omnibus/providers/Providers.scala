package borg.omnibus.providers

import borg.omnibus.gtfs.GtfsDatabase

import scala.concurrent.duration._

case class Provider(id: String, gtfsrt: GtfsrtApis, gtfs: GtfsDatabase)

object Providers {
  lazy val Bart = Provider(
    "bart",
    GtfsrtApis(
      15.seconds,
      TripUpdateApi("http://api.bart.gov/gtfsrt/tripupdate.aspx"),
      AlertsApi("http://api.bart.gov/gtfsrt/alerts.aspx")
    ),
    GtfsDatabase.fromResource("/providers/bart-gtfs.zip"))

  lazy val ArlingtonTransit = Provider(
    "art",
    GtfsrtApis(
      30.seconds,
      TripUpdateApi("http://realtime.commuterpage.com/rtt/public/utility/gtfsrealtime.aspx/tripupdate")
    ),
    GtfsDatabase.fromResource("/providers/art-gtfs.zip")
  )
  lazy val providers = List(Bart, ArlingtonTransit)
}

trait ProvidersComponent {
  def providers: Iterable[Provider]
}
