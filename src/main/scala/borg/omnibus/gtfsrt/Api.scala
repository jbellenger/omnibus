package borg.omnibus.gtfsrt

import akka.http.model.Uri
import com.google.transit.realtime.GtfsRealtime

import scala.collection.JavaConversions._

sealed trait Api extends Any {
  def uri: Uri
  def parse(header: RecordHeader, msg: GtfsRealtime.FeedMessage): Iterable[Record]
}

object Api {
  implicit def apiToUri(api: Api): Uri = api.uri
}

case class TripUpdateApi(uri: Uri) extends AnyVal with Api {
  override def parse(header: RecordHeader, msg: GtfsRealtime.FeedMessage): Iterable[Record] = {
    msg.getEntityList.collect {
      case x if x.hasTripUpdate =>
        TripUpdateRecord(header, x.getTripUpdate)
    }
  }
}

case class AlertsApi(uri: Uri) extends AnyVal with Api {
  override def parse(header: RecordHeader, msg: GtfsRealtime.FeedMessage): Iterable[Record] = {
    msg.getEntityList.collect {
      case x if x.hasAlert =>
        AlertRecord(header, x.getAlert)
    }
  }
}

case class VehiclePositionsApi(uri: Uri) extends AnyVal with Api {
  override def parse(header: RecordHeader, msg: GtfsRealtime.FeedMessage): Iterable[Record] = {
    msg.getEntityList.collect {
      case x if x.hasVehicle =>
        VehiclePositionRecord(header, x.getVehicle)
    }
  }
}
