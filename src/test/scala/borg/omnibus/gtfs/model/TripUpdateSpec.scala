package borg.omnibus.gtfs.model

import borg.omnibus.gtfsrt.TripUpdate
import org.scalatest._

class TripUpdateSpec extends WordSpec with Matchers {
  "A TripUpdate" should {
    "roundtrip to mongo" in {
      MockTripUpdates foreach {tu =>
        TripUpdate(tu.toMongo) shouldBe tu
      }
    }
  }
}

