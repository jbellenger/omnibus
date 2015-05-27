package borg.omnibus

import java.net.URL

package object gtfs {
  implicit def toUrl(s: String): URL = new URL(s)
  implicit def toOptionURL(s: Option[String]): Option[URL] = s.map(toUrl)
  implicit def toOptionLong(s: Option[String]): Option[Long] = s.map(_.toLong)
  implicit def toOptionDouble(s: Option[String]): Option[Double] = s.map(_.toDouble)

  implicit class RichString(val s: String) extends AnyVal {
    def safeToInt: Int = s match {
      case "" => 0
      case x => x.toInt
    }

    def safeToLong: Long = s match {
      case "" => 0L
      case x => x.toLong
    }
  }
}
