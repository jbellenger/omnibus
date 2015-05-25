package borg.omnibus.gtfsrt

import org.scalatest._

class AlertRecordSpec extends WordSpec with Matchers {
  "An AlertRecord" should {
    "roundtrip to mongo" in {
      MockAlertRecords foreach {ar =>
        AlertRecord(ar.toMongo) shouldBe ar
      }
    }
  }
}
