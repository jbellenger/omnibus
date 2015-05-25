package borg.omnibus.app

import akka.actor.ActorSystem
import borg.omnibus.gtfsrt.{AlertRecord, TripUpdateRecord, VehiclePositionRecord}
import borg.omnibus.providers.{Providers, ProvidersComponent}
import borg.omnibus.scrape.{ScrapeDriverComponent, ScraperComponent, StreamScraper}
import borg.omnibus.store._
import com.mongodb.casbah.MongoClient

object ScraperMain extends App {
  implicit val system = ActorSystem("scraper")

  val env = new ScrapeDriverComponent
      with ProvidersComponent
      with ScraperComponent
      with StoresComponent {

    override lazy val providers = Providers.providers

    private lazy val db = MongoClient("localhost", 27017).getDB("omnibus")

    override lazy val tripUpdatesStore =
      new MongoCollectionStore(db, MongoIndices.TripUpdate, "tripupdate")
        .wrap(TripUpdateRecord.Codec)

    override lazy val alertStore =
      new MongoCollectionStore(db, MongoIndices.TripUpdate, "alert")
        .wrap(AlertRecord.Codec)

    override lazy val vehiclePositionStore =
      new MongoCollectionStore(db, MongoIndices.VehiclePosition, "vehicleposition")
        .wrap(VehiclePositionRecord.Codec)

    override lazy val scraper = new StreamScraper

    override val scrapeDriver = system.actorOf(ScrapeDriver.props)
  }

  system.awaitTermination()
}
