package borg.omnibus.providers

import org.scalatest._

class ProvidersSpec extends WordSpec with Matchers {
  "Providers" when {
    Providers.providers foreach { prov =>
      s"provider is ${prov.id}" should {
        s"load required gtfs resources" in {
          val m = prov.gtfs
          m.agencies should not be empty
          m.stops should not be empty
          m.routes should not be empty
          m.trips should not be empty
          m.stopTimes should not be empty
          m.calendar should not be empty
        }
      }
    }
  }
}
