package borg.omnibus.gtfs.model

import com.mongodb.casbah.Imports._

object MongoImplicits {
  implicit class RichMongoDBObject(o: MongoDBObject) {
    def objectSeq(key: String): Seq[MongoDBObject] = {
      o.as[MongoDBList](key) collect {
        case x: MongoDBObject => x
      }
    }
  }
}
