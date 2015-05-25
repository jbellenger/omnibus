package borg.omnibus

import java.net.URL

package object gtfs2 {
  sealed trait Field extends Any {
    def key: String
  }
  case class RequiredField(key: String) extends AnyVal with Field
  case class OptionalField(key: String) extends AnyVal with Field

  case class Values(values: Map[Field, String]) {
    def apply(f: RequiredField): String = values(f)
    def apply(f: OptionalField): Option[String] = values.get(f)
  }

  implicit def toUrl(s: String): URL = new URL(s)
  implicit def toOptionURL(s: Option[String]): Option[URL] = s.map(toUrl)
  implicit def toOptionLong(s: Option[String]): Option[Long] = s.map(_.toLong)
  implicit def toOptionDouble(s: Option[String]): Option[Double] = s.map(_.toDouble)

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
}
