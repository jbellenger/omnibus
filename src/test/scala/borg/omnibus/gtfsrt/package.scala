package borg.omnibus

import borg.omnibus.gtfs.{Direction0, Direction1}
import borg.omnibus.providers.ProviderId

import scala.concurrent.duration._

package object gtfsrt {
  lazy val MockDepartures = List(
    Departure(Duration.Zero, Int.MinValue),
    Departure(Int.MaxValue.seconds, Int.MaxValue))

  lazy val MockSnapshotHeaders = List(
    SnapshotHeader(Long.MinValue),
    SnapshotHeader(Long.MaxValue))

  lazy val MockTripDescriptors = List(
    TripDescriptor("mock-tripid", Some(Direction0), "mock-routeid"),
    TripDescriptor("mock-tripid", Some(Direction1), "mock-routeid"),
    TripDescriptor("mock-tripid", None, "mock-routeid"))

  lazy val MockStopTimeUpdates = for {
      stopSeq <- List(Int.MinValue, Int.MaxValue)
      dept <- MockDepartures
      stopId <- List("", null, "mock-stopid")
    } yield StopTimeUpdate(stopSeq, dept, stopId)

  lazy val MockTripUpdates = for {
      td <- MockTripDescriptors
      stus <- permute(MockStopTimeUpdates)
    } yield TripUpdate(td, stus)

  lazy val MockGtfsrtSnapshots = for {
      header <- MockSnapshotHeaders
      items <- permute(MockTripUpdates)
      providerId = ProviderId("mock-provider")
    } yield GtfsrtSnapshot(providerId, header, items)

  def permute[T](l: List[T]): Iterable[List[T]] =
    List(Nil, List(l.head), l)
}
