package borg.omnibus.store

import org.scalatest._

class StoreSpec extends WordSpec with Matchers {
  class StringStore extends Store[String] {
    var s: String = null
    override def save(obj: String): Unit = {
      s = obj
    }
    override def init(): Unit = {}
  }

  object IntCodec extends Codec[Int, String] {
    override def encode(a: Int): String = a.toString
    override def decode(b: String): Int = b.toInt
  }

  "A Store" when {
    "#wrap" should {
      "encode" in {
        val ss = new StringStore()
        val is = ss wrap IntCodec
        is.save(1)
        ss.s shouldBe "1"
      }
    }
  }
}
