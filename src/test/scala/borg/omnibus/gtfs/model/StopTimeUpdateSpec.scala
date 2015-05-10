package borg.omnibus.gtfs.model

import borg.omnibus.gtfsrt.StopTimeUpdate
import org.scalatest._

class StopTimeUpdateSpec extends WordSpec with Matchers {
  "A StopTimeUpdate" should {
    "roundtrip to mongo" in {
      MockStopTimeUpdates foreach {stu =>
        StopTimeUpdate(stu.toMongo) shouldBe stu
      }
    }
  }
}
