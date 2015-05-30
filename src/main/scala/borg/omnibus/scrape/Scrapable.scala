package borg.omnibus.scrape

import borg.omnibus.gtfsrt.GtfsrtApis
import borg.omnibus.providers.ProviderId

trait Scrapable {
  def id: ProviderId
  def gtfsrt: GtfsrtApis
}
