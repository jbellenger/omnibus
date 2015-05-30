package borg.omnibus.scrape

import borg.omnibus.gtfsrt.Record

import scala.concurrent.Future

trait ScraperComponent {
  def scraper: Scraper
}

trait Scraper {
  def scrape(scrapable: Scrapable): Future[Iterable[Record]]
  def shutdown(): Unit
}
