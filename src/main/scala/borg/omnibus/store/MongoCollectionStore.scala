package borg.omnibus.store

import com.mongodb.casbah.Imports._
import grizzled.slf4j.Logging

class MongoCollectionStore(db: MongoDB, indices: MongoIndices, collectionName: String)
    extends Store[MongoDBObject]
    with Logging {

  private val coll = db(collectionName)

  override def save(obj: MongoDBObject): Unit = {
    coll += obj
  }

  override def init(): Unit = {
    indices foreach {
      case MongoIndex(keys, opts) => coll.createIndex(keys, opts)
    }
  }
}
