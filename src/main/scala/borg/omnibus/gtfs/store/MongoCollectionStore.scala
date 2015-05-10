package borg.omnibus.gtfs.store

import borg.omnibus.gtfs.model.GtfsrtSnapshot
import com.mongodb.casbah.Imports._

class MongoCollectionStore(host: String, port: Int, dbName: String, collectionName: String) extends Store[GtfsrtSnapshot] {
  private val client = MongoClient(host, port)
  private val coll = client.getDB(dbName)(collectionName)

  override def save(msg: GtfsrtSnapshot): Unit = {
    coll += msg.toMongo
  }
}
