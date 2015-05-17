package borg.omnibus.providers

import scala.concurrent.duration._

case class Provider(id: ProviderId, gtfsrt: GtfsrtMeta, gtfsModels: GtfsModels)

object Providers {
  lazy val Bart = Provider(
    ProviderId("bart"),
    GtfsrtMeta("http://api.bart.gov:80/gtfsrt/tripupdate.aspx", 15.seconds),
    GtfsModels.fromResource("/providers/bart-gtfs.zip"))

  lazy val providers = List(Bart)
}

trait ProvidersComponent {
  def providers: Iterable[Provider]
}
