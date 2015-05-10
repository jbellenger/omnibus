package borg.omnibus.gtfs.app

import akka.actor.ActorSystem
import borg.omnibus.gtfs.model.GtfsrtSnapshot
import borg.omnibus.gtfs.providers.{Providers, ProvidersComponent}
import borg.omnibus.gtfs.scrape.{AkkaScraper, ScrapeDriverComponent, Scraper, ScraperComponent}
import borg.omnibus.gtfs.store.{MongoCollectionStore, Store, StoreComponent}

object ScraperMain extends App {
  implicit val system = ActorSystem("scraper")

  val env = new ScrapeDriverComponent
      with ProvidersComponent
      with ScraperComponent
      with StoreComponent {

    override lazy val providers = Providers.providers
    override lazy val store: Store[GtfsrtSnapshot] = new MongoCollectionStore("localhost", 27017, "omnibus", "gtfsrt")
    override lazy val scraper: Scraper = new AkkaScraper

    override val scrapeDriver = system.actorOf(ScrapeDriver.props)
  }

  system.awaitTermination()
}
