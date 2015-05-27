package borg.omnibus.gtfs

abstract class Model[T] {
  var fields = Map.empty[String, Field]

  protected def required(key: String): RequiredField = {
    val f = RequiredField(key)
    fields += key -> f
    f
  }

  protected def optional(key: String): OptionalField = {
    val f = OptionalField(key)
    fields += key -> f
    f
  }

  def apply(values: Values): T
}
