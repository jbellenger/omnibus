package borg.omnibus.gtfs.model

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
