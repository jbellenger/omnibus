package borg.omnibus.providers

import borg.omnibus.gtfs.GtfsDatabase
import borg.omnibus.gtfsrt.{AlertsApi, GtfsrtApis, TripUpdateApi, VehiclePositionsApi}

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

  // https://www.capmetro.org/metrolabs/
  /* api urls are probably wrong
  lazy val CapitalMetro = Provider(
    "capmetro",
    GtfsrtApis(
      15.seconds, // could not find info, just a guess
      VehiclePositionsApi("https://data.texas.gov/download/i5qp-g5fd/application/octet-stream"),
      AlertsApi("https://data.texas.gov/download/nusn-7fcn/application/octet-stream")
    ),
    GtfsDatabase.fromResource("/providers/capmetro-gtfs.zip")
  )
  */

  // http://www.vre.org/programs/PDF/MobileDevelopersGuideweb.pdf
  lazy val VirginiaRailwayExpress = Provider(
    "vre",
    GtfsrtApis(
      1.minute,
      VehiclePositionsApi("http://www.vre.org/gtfs-realtime/VehiclePositionFeed"),
      TripUpdateApi("http://www.vre.org/gtfs-realtime/TripUpdateFeed")
    ),
    GtfsDatabase.fromResource("/providers/vre-gtfs.zip")
  )

  // http://www.yrt.ca/en/aboutus/gtfsdownload.asp
  lazy val YorkRegionalTransit = Provider(
    "york",
    GtfsrtApis(
      1.minute, // no information, just a guess
      TripUpdateApi("http://rtu.york.ca/gtfsrealtime/TripUpdates"),
      VehiclePositionsApi("http://rtu.york.ca/gtfsrealtime/VehiclePositions"),
      AlertsApi("http://rtu.york.ca/gtfsrealtime/ServiceAlerts")
    ),
    GtfsDatabase.fromResource("/providers/york-gtfs.zip")
  )

  lazy val providers = List(Bart, ArlingtonTransit, VirginiaRailwayExpress, YorkRegionalTransit)
}

trait ProvidersComponent {
  def providers: Iterable[Provider]
}

object Test {
  def main(args: Array[String]): Unit = {
    while (true) {
      val start = System.currentTimeMillis()
      GtfsDatabase.fromResource("/providers/york-gtfs.zip")
      println(System.currentTimeMillis() - start)
    }
  }
}
