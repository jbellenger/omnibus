package borg.omnibus.gtfs.providers

import borg.omnibus.gtfs.model.{GtfsModels, GtfsrtMeta}

import scala.concurrent.duration._

case class Provider(gtfsrt: GtfsrtMeta, gtfsModels: GtfsModels)

object Providers {
  lazy val Bart = Provider(
    GtfsrtMeta("http://api.bart.gov:80/gtfsrt/tripupdate.aspx", 15.seconds),
    GtfsModels.fromResource("/bart-gtfs.zip"))

  lazy val providers = List(Bart)
}

trait ProvidersComponent {
  def providers: Iterable[Provider]
}
