package borg.omnibus.gtfs

case class FareAttribute(
  fareId: String,
  price: Double,
  currencyType: String,
  paymentMethod: Int,
  transfers: Int,
  transferDuration: Option[Long])

object FareAttributeModel extends Model[FareAttribute] {
  val FareId = required("fare_id")
  val Price = required("price")
  val CurrencyType = required("currency_type")
  val PaymentMethod = required("payment_method")
  val Transfers = required("transfers")
  val TransferDuration = optional("transfer_duration")

  override def apply(values: Values) =
    FareAttribute(
      values(FareId),
      values(Price).toDouble,
      values(CurrencyType),
      values(PaymentMethod).safeToInt,
      values(Transfers).safeToInt,
      values(TransferDuration))
}
