package borg.omnibus.gtfs.model

import org.scalatest._

class DepartureSpec extends WordSpec with Matchers {
  "A Departure" should {
    "roundtrip to mongo" in {
      MockDepartures foreach {md =>
        Departure(md.toMongo) shouldBe md
      }
    }
  }
}
