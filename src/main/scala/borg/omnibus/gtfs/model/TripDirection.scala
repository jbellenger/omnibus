package borg.omnibus.gtfs.model

import scala.util.Try

sealed trait TripDirection {
  def toInt: Int
}

case object Direction0 extends TripDirection {
  override def toInt: Int = 0
}
case object Direction1 extends TripDirection {
  override def toInt: Int = 1
}

object TripDirection {
  val pf: PartialFunction[Int, TripDirection] = {
    case 0 => Direction0
    case 1 => Direction1
  }

  def apply(string: String) = pf(string.toInt)
  def apply(int: Int) = pf(int)

  def unapply(string: String): Option[TripDirection] = {
    Try {
      pf.lift(string.toInt)
    }.recover {
      case x => None
    }.get
  }
}

