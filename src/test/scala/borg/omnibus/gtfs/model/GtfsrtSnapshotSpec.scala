package borg.omnibus.gtfs.model

import borg.omnibus.gtfsrt.GtfsrtSnapshot
import org.scalatest._

class GtfsrtSnapshotSpec extends WordSpec with Matchers {
  "A GtfsrtSnapshot" should {
    "roundtrip to mongo" in {
      MockGtfsrtSnapshots foreach {x =>
        GtfsrtSnapshot(x.toMongo) shouldBe x
      }
    }
  }
}
