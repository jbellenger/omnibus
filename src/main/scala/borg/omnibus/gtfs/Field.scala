package borg.omnibus.gtfs

sealed trait Field extends Any {
  def key: String
}

case class RequiredField(key: String) extends AnyVal with Field
case class OptionalField(key: String) extends AnyVal with Field

