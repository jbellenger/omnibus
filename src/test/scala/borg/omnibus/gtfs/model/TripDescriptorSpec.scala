package borg.omnibus.gtfs.model

import org.scalatest._

class TripDescriptorSpec extends WordSpec with Matchers {
  "A TripDescriptor" should {
    "roundtrip to mongo" in {
      MockTripDescriptors foreach {td =>
        TripDescriptor(td.toMongo) shouldBe td
      }
    }
  }
}
