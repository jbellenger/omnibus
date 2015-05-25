package borg.omnibus.store

import com.mongodb.casbah.commons.MongoDBObject

case class MongoIndex(keys: MongoDBObject, options: MongoDBObject)
case class MongoIndices(indices: Iterable[MongoIndex]) extends Iterable[MongoIndex] {
  override def iterator: Iterator[MongoIndex] = indices.iterator
}

object MongoIndices {
  val TripUpdate = MongoIndices(Nil)
  val VehiclePosition = MongoIndices(Nil)
  val Alert = MongoIndices(Nil)
}
