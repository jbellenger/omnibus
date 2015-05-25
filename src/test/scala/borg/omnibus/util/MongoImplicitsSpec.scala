package borg.omnibus.util

import borg.omnibus.util.MongoImplicits._
import com.mongodb.casbah.Imports._
import org.scalatest._

class MongoImplicitsSpec extends WordSpec with Matchers {
  def wrap(xs: Any*): MongoDBObject =
    MongoDBObject(
      "items" -> MongoDBList.concat(xs)
    )

  "A MongoImplicits" when {
    "objectSeq" should {
      "handle MongoDBObjects" in {
        val o = wrap(MongoDBObject.empty, MongoDBObject.empty)
        o.objectSeq("items") should have size 2
      }

      "wrap BasicDBObjects in MongoDBObjects" in {
        val o = wrap(new BasicDBObject(), new BasicDBObject())
        o.objectSeq("items") should be(List(MongoDBObject.empty, MongoDBObject.empty))
      }

      "handle empty lists" in {
        val o = wrap()
        o.objectSeq("items") should have size 0
      }

      "throw NoSuchElementException for missing keys" in {
        val o: MongoDBObject = MongoDBObject.empty
        intercept[NoSuchElementException] {
          o.objectSeq("items")
        }
      }
    }
  }
}
