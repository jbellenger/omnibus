package borg.omnibus.store

import borg.omnibus.gtfsrt.GtfsrtSnapshot

trait Store[T] {
  def save(obj: T)
  def init()

  def wrap[U](codec: Codec[U, T]): Store[U] = {
    val underlying = this
    new Store[U] {
      override def save(obj: U): Unit = {
        underlying.save(codec.encode(obj))
      }
      override def init(): Unit = {
        underlying.init()
      }
    }
  }
}

trait StoresComponent {
  def gtfsrtStore: Store[GtfsrtSnapshot]
}
