package borg.omnibus.gtfsrt

import org.scalatest._

class VehiclePositionRecordSpec extends WordSpec with Matchers {
  "A VehiclePositionRecord" should {
    "roundtrip to mongo" in {
      MockVehiclePositionRecords foreach {vp =>
        VehiclePositionRecord(vp.toMongo) shouldBe vp
      }
    }
  }
}