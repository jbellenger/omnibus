package borg.omnibus.gtfs.store

import borg.omnibus.gtfs.model.GtfsrtSnapshot

trait Store[T] {
  def save(obj: T)
}

trait StoreComponent {
  def store: Store[GtfsrtSnapshot]
}
