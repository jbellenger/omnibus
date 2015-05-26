package borg.omnibus.gtfs2

case class Transfer(
  fromStopId: String,
  toStopId: String,
  transferType: Int,
  minTransferTime: Option[Int])

object TransferModel extends Model[Transfer] {
  val FromStopId = required("from_stop_id")
  val ToStopId = required("to_stop_id")
  val TransferType = required("transfer_type")
  val MinTransferTime = optional("min_transfer_time")

  override def apply(values: Values) =
    Transfer(
      values(FromStopId),
      values(ToStopId),
      values(TransferType).toInt,
      values(MinTransferTime).map(_.toInt))
}

