package borg.omnibus.providers

import org.scalatest._

class ProvidersSpec extends WordSpec with Matchers {
  "Providers" should {
    Providers.providers.foreach {prov =>
      s"load provider ${prov.id}" in {
        val m = prov.gtfsModels
        m.routes.items should not be empty
        m.trips.items should not be empty
        m.stopTimes.items should not be empty
        m.stops.items should not be empty
      }
    }
  }
}
