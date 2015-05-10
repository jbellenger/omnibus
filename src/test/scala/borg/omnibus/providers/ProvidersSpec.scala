package borg.omnibus.providers

import org.scalatest._

class ProvidersSpec extends WordSpec with Matchers {
  "Providers" when {
    "#Bart" should {
      "load gtfs models" in {
        val models = Providers.Bart.gtfsModels
      }
    }
  }
}
