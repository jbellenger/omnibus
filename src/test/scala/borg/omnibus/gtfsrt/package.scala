package borg.omnibus

import borg.omnibus.gtfs.{Direction0, Direction1}

import scala.concurrent.duration._

package object gtfsrt {
  lazy val MockTimeRanges = List(
    TimeRange(Long.MinValue, Long.MaxValue))

  lazy val MockAlertRecords = for {
    trs <- permute(MockTimeRanges)
    header <- MockRecordHeaders
  } yield AlertRecord(header, trs, 0, 0, "mock-url")

  lazy val MockDepartures = List(
    Departure(Duration.Zero, Int.MinValue),
    Departure(Int.MaxValue.seconds, Int.MaxValue))

  lazy val MockRecordHeaders = List(
    RecordHeader("mock-provider", 0L),
    RecordHeader("mock-provider", Long.MaxValue))

  lazy val MockTripDescriptors = List(
    TripDescriptor("mock-tripid", Some(Direction0), "mock-routeid"),
    TripDescriptor("mock-tripid", Some(Direction1), "mock-routeid"),
    TripDescriptor("mock-tripid", None, "mock-routeid"))

  lazy val MockStopTimeUpdates = for {
      stopSeq <- List(Int.MinValue, Int.MaxValue)
      dept <- MockDepartures
      stopId <- List("", null, "mock-stopid")
    } yield StopTimeUpdate(stopSeq, dept, stopId)

  lazy val MockTripUpdateRecords = for {
      header <- MockRecordHeaders
      td <- MockTripDescriptors
      stus <- permute(MockStopTimeUpdates)
    } yield TripUpdateRecord(header, td, stus)

  lazy val MockPositions = for {
    flt <- Seq(Float.MinValue, 0f, Float.MaxValue)
    optFlt <- Seq(Some(flt), None)
  } yield Position(flt, flt, optFlt, optFlt)

  lazy val MockVehiclePositionRecords = for {
    header <- MockRecordHeaders
    pos <- MockPositions
    stopSeq <- Seq(0, Int.MaxValue)
    stamp <- Seq(0L, Long.MaxValue)
  } yield VehiclePositionRecord(header, pos, stopSeq, "mock-stopId", stamp)

  def permute[T](l: List[T]): Iterable[List[T]] =
    List(Nil, List(l.head), l)
}
