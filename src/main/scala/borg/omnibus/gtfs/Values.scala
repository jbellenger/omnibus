package borg.omnibus.gtfs

case class Values(values: Map[Field, String]) {
  def apply(f: RequiredField): String = values(f)
  def apply(f: OptionalField): Option[String] = values.get(f)
}

