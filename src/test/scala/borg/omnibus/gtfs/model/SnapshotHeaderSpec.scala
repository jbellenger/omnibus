package borg.omnibus.gtfs.model

import borg.omnibus.gtfsrt.SnapshotHeader
import org.scalatest._

class SnapshotHeaderSpec extends WordSpec with Matchers {
  "A SnapshotHeader" should {
    "roundtrip to mongo" in {
      MockSnapshotHeaders foreach {sh =>
        SnapshotHeader(sh.toMongo) shouldBe sh
      }
    }
  }
}
