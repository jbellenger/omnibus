package borg.omnibus.gtfsrt

import org.scalatest._

class RecordHeaderSpec extends WordSpec with Matchers {
  "A RecordHeader" should {
    "roundtrip to mongo" in {
      MockRecordHeaders foreach {sh =>
        RecordHeader(sh.toMongo) shouldBe sh
      }
    }
  }
}
