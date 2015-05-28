package borg.omnibus.util

import org.scalatest._

class BOMFilterSpec extends WordSpec with Matchers {
  "BOMFilter" should {
    "strip byte order marks" in {
      val s = "\uFEFFabc"
      s.filter(BOMFilter) shouldBe "abc"
    }
  }
}
