package borg.omnibus.gtfs

case class FareRule(
  fareId: String,
  routeId: Option[String],
  originId: Option[String],
  destinationId: Option[String],
  containsId: Option[String])

object FareRuleModel extends Model[FareRule] {
  val FareId = required("fare_id")
  val RouteId = optional("route_id")
  val OriginId = optional("origin_id")
  val DestinationId = optional("destination_id")
  val ContainsId = optional("contains_id")

  override def apply(values: Values) =
    FareRule(
      values(FareId),
      values(RouteId),
      values(OriginId),
      values(DestinationId),
      values(ContainsId))
}

