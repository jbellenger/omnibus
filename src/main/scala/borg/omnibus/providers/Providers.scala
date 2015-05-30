package borg.omnibus.providers

import borg.omnibus.gtfs.GtfsDatabase
import borg.omnibus.gtfsrt.{AlertsApi, GtfsrtApis, TripUpdateApi, VehiclePositionsApi}
import borg.omnibus.scrape.Scrapable

import scala.concurrent.duration._

case class Provider(id: ProviderId, gtfsrt: GtfsrtApis, gtfs: GtfsDatabase) extends Scrapable

object Providers {
  lazy val providers = List(
    Provider(
      ProviderId("bart", "Bay Area Rapid Transit"),
      GtfsrtApis(
        15.seconds,
        TripUpdateApi("http://api.bart.gov/gtfsrt/tripupdate.aspx"),
        AlertsApi("http://api.bart.gov/gtfsrt/alerts.aspx")
      ),
      GtfsDatabase.fromResource("/providers/bart-gtfs.zip")
    ),

    Provider(
      ProviderId("art", "Arlington Transit"),
      GtfsrtApis(
        30.seconds,
        TripUpdateApi("http://realtime.commuterpage.com/rtt/public/utility/gtfsrealtime.aspx/tripupdate")
      ),
      GtfsDatabase.fromResource("/providers/art-gtfs.zip")
    ),

    // http://www.vre.org/programs/PDF/MobileDevelopersGuideweb.pdf
    Provider(
      ProviderId("vre", "Virginia Railway Express"),
      GtfsrtApis(
        1.minute,
        VehiclePositionsApi("http://www.vre.org/gtfs-realtime/VehiclePositionFeed"),
        TripUpdateApi("http://www.vre.org/gtfs-realtime/TripUpdateFeed")
      ),
      GtfsDatabase.fromResource("/providers/vre-gtfs.zip")
    ),

    // http://www.yrt.ca/en/aboutus/gtfsdownload.asp
    Provider(
      ProviderId("york", "York Regional Transit"),
      GtfsrtApis(
        1.minute, // no information, just a guess
        TripUpdateApi("http://rtu.york.ca/gtfsrealtime/TripUpdates"),
        VehiclePositionsApi("http://rtu.york.ca/gtfsrealtime/VehiclePositions"),
        AlertsApi("http://rtu.york.ca/gtfsrealtime/ServiceAlerts")
      ),
      GtfsDatabase.fromResource("/providers/york-gtfs.zip")
    )
  )
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
