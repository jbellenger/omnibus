package borg.omnibus.gtfs.scrape

import borg.omnibus.gtfs.providers.Provider
import borg.omnibus.gtfs.scrape.Scraper.ScrapeResult

import scala.concurrent.Future

trait ScraperComponent {
  def scraper: Scraper
}

trait Scraper {
  def scrape(provider: Provider): Future[ScrapeResult]
  def shutdown(): Unit
}

object Scraper {
  type ScrapeResult = Boolean
}
