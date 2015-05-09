package borg.omnibus.gtfs.model

sealed trait TripDirection

case object Direction0 extends TripDirection
case object Direction1 extends TripDirection

object TripDirection {
  val pf: PartialFunction[String, TripDirection] = {
    case "0" => Direction0
    case "1" => Direction1
  }

  def apply(string: String) = pf(string)

  def unapply(string: String): Option[TripDirection] =
    pf.lift(string)
}

