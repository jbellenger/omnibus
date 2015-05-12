package borg.omnibus.scrape

import borg.omnibus.gtfsrt.GtfsrtSnapshot
import borg.omnibus.providers.Provider

import scala.concurrent.Future

trait ScraperComponent {
  def scraper: Scraper
}

trait Scraper {
  def scrape(provider: Provider): Future[GtfsrtSnapshot]
  def shutdown(): Unit
}
