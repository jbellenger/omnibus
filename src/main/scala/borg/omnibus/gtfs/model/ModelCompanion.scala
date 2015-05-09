package borg.omnibus.gtfs.model

trait ModelCompanion[T] {
  def parse(lines: Iterable[String]): T

  protected def toMap[T](lines: Iterable[String], parse: String => T, id: T => String): Map[String, T] =
    lines.map(parse).map(t => id(t) -> t).toMap
}
