package borg.omnibus.scrape

import borg.omnibus.gtfsrt.GtfsrtSnapshot
import borg.omnibus.providers.Provider
import borg.omnibus.scrape.Scraper.ScrapeResult

import scala.concurrent.Future

trait ScraperComponent {
  def scraper: Scraper
}

trait Scraper {
  def scrape(provider: Provider): Future[ScrapeResult]
  def shutdown(): Unit
}

object Scraper {
  type ScrapeResult = GtfsrtSnapshot
}
