package borg.omnibus.util

import com.mongodb.casbah.Imports._

object MongoImplicits {
  implicit class RichMongoDBObject(o: MongoDBObject) {
    def objectSeq(key: String): Seq[MongoDBObject] = {
      o.as[MongoDBList](key) collect {
        case x: MongoDBObject => x
        case x: BasicDBObject => new MongoDBObject(x)
      }
    }
  }
}
