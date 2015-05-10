package borg.omnibus.gtfs.model

private[model] trait ModelCompanion[T] {
  def parse(lines: Iterable[String]): T

  protected def toMap[U](lines: Iterable[String], parse: String => U, id: U => String): Map[String, U] =
    lines.map(parse).map(t => id(t) -> t).toMap
}
