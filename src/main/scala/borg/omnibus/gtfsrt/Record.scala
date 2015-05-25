package borg.omnibus.gtfsrt

import borg.omnibus.store.Codec
import com.mongodb.casbah.commons.MongoDBObject

trait Record {
  def toMongo: MongoDBObject
}

object Record {
  class RecordCodec[A <: Record](parse: MongoDBObject => A) extends Codec[A, MongoDBObject] {
    override def encode(a: A): MongoDBObject = a.toMongo
    override def decode(b: MongoDBObject): A = parse(b)
  }
}
