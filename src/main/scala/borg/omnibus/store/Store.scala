package borg.omnibus.store

import borg.omnibus.gtfsrt.GtfsrtSnapshot

trait Store[T] {
  def save(obj: T)
}

trait StoreComponent {
  def store: Store[GtfsrtSnapshot]
}
