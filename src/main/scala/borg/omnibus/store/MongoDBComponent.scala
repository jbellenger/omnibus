package borg.omnibus.store

import com.mongodb.casbah.MongoDB

trait MongoDBComponent {
  def mongoDB: MongoDB
}
