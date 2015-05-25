package borg.omnibus.gtfsrt

import org.scalatest._

class TripUpdateRecordSpec extends WordSpec with Matchers {
  "A TripUpdateRecord" should {
    "roundtrip to mongo" in {
      MockTripUpdateRecords foreach {tu =>
        TripUpdateRecord(tu.toMongo) shouldBe tu
      }
    }
  }
}

