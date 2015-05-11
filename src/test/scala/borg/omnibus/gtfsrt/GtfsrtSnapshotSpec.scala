package borg.omnibus.gtfsrt

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
