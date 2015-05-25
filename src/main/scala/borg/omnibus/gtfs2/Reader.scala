package borg.omnibus.gtfs2

class Reader[T](model: Model[T], header: String) {
  val headerArray = header.split(",").map(_.trim)
  val headerSet = headerArray.toSet

  require(model.fields.values.forall {
    case RequiredField(key) => headerSet(key)
    case _ => true
  })

  def map(line: String): T = {
    val pairs = headerArray.zip(line.split(",", -1).map(_.trim())) flatMap {
      case (fieldKey, value) => model.fields(fieldKey) match {
        case f: OptionalField if value == "" =>
          None
        case f if value != "" =>
          Some(f -> value)
      }
    }
    model(Values(pairs.toMap))
  }
}
