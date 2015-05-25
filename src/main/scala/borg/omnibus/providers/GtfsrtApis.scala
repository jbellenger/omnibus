package borg.omnibus.providers

import scala.concurrent.duration.FiniteDuration

case class GtfsrtApis(interval: FiniteDuration, apis: Api*)
