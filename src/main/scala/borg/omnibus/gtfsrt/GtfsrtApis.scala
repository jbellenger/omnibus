package borg.omnibus.gtfsrt

import scala.concurrent.duration.FiniteDuration

case class GtfsrtApis(interval: FiniteDuration, apis: Api*)
