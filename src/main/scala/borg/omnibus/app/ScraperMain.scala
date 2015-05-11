package borg.omnibus.app

import akka.actor.ActorSystem
import borg.omnibus.gtfsrt.GtfsrtSnapshot
import borg.omnibus.providers.{Providers, ProvidersComponent}
import borg.omnibus.scrape.{AkkaScraper, ScrapeDriverComponent, ScraperComponent}
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
    override lazy val gtfsrtStore =
      new MongoCollectionStore(db, MongoIndices.Gtfsrt, "gtfsrt")
        .wrap(GtfsrtSnapshot.MongoCodec)

    override lazy val scraper = new AkkaScraper

    override val scrapeDriver = system.actorOf(ScrapeDriver.props)
  }

  system.awaitTermination()
}
