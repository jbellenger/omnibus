package borg.omnibus.scrape

import borg.omnibus.gtfsrt.Record
import borg.omnibus.providers.Provider

import scala.concurrent.Future

trait ScraperComponent {
  def scraper: Scraper
}

trait Scraper {
  def scrape(provider: Provider): Future[Iterable[Record]]
  def shutdown(): Unit
}
