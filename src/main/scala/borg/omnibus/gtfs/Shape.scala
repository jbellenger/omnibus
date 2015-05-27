package borg.omnibus.gtfs

case class Shape(
  shapeId: String,
  shapePtLat: String,
  shapePtLon: String,
  shapePtSequence: Int,
  shapeDistTraveled: Option[Double])


object ShapeModel extends Model[Shape] {
  val ShapeId = required("shape_id")
  val ShapePtLat = required("shape_pt_lat")
  val ShapePtLon = required("shape_pt_lon")
  val ShapePtSequence = required("shape_pt_sequence")
  val ShapeDistTraveled = optional("shape_dist_traveled")

  override def apply(values: Values) =
    Shape(
      values(ShapeId),
      values(ShapePtLat),
      values(ShapePtLon),
      values(ShapePtSequence).toInt,
      values(ShapeDistTraveled))
}
