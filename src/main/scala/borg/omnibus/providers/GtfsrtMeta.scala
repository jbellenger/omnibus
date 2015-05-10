package borg.omnibus.providers

import akka.http.model.Uri

import scala.concurrent.duration.FiniteDuration

case class GtfsrtMeta(uri: Uri, pollInterval: FiniteDuration)

object GtfsrtMeta {
  def apply(uri: String, pollInterval: FiniteDuration): GtfsrtMeta =
    GtfsrtMeta(Uri(uri), pollInterval)
}
