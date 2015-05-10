package borg.omnibus.gtfs.app

import akka.actor.ActorSystem
import borg.omnibus.gtfs.providers.{Providers, ProvidersComponent}
import borg.omnibus.gtfs.scrape.{AkkaScraper, ScrapeDriverComponent, Scraper, ScraperComponent}

object ScrapeMain extends App {
  implicit val system = ActorSystem("scrape")

  val env = new ScrapeDriverComponent
      with ProvidersComponent
      with ScraperComponent {

    override def providers = Providers.providers
    override val scrapeDriver = system.actorOf(ScrapeDriver.props)
    override def scraper: Scraper = new AkkaScraper
  }

  system.awaitTermination()
}
