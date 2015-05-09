package borg.omnibus.gtfs

import com.google.transit.realtime.GtfsRealtime
import com.google.transit.realtime.GtfsRealtime.FeedMessage

import scala.concurrent.{ExecutionContext, Future}

/*
trait GtfsrtProvider {
  def poll(): Future[GtfsRealtime.FeedMessage]
}

private[gtfs] class ApiProvider(endpoint: String, ec: ExecutionContext) extends GtfsrtProvider {
  override def poll(): Future[FeedMessage] = {
    null
  }
}

object GtfsrtProvider {
  def apply(endpoint: String): GtfsrtProvider =
    new ApiProvider(endpoint)
}
*/
