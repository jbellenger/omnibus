package borg.omnibus.csv

import borg.omnibus.gtfs.{Model, OptionalField, RequiredField, Values}
import org.parboiled2.ParserInput

class Reader[T](model: Model[T], headers: Iterable[String]) {
  val headerSet = headers.toSet

  val missingRequiredFields = model.fields.values collect {
    case RequiredField(key) if !headerSet(key) => key
  }
  require(missingRequiredFields.isEmpty, s"missing required fields: ${missingRequiredFields.mkString(",")}")

  def map(values: Seq[String]): T = {
    val pairs = headers.zip(values) flatMap {
      case (fieldKey, value) => model.fields.get(fieldKey) flatMap {
        case f: OptionalField if value == "" =>
          None
        case f: RequiredField if value == "" =>
          Some(f -> "")
        case f if value != "" =>
          Some(f -> value)
      }
    }
    model(Values(pairs.toMap))
  }
}

object Reader {
  def apply[T](body: ParserInput, model: Model[T]): Iterable[T] = {
    CsvParser(body) match {
      case Right(CsvFile(Some(header), rows)) =>
        val reader = new Reader(model, header.fields)
        rows.map(r => reader.map(r.fields))
      case Left(err) =>
        throw new IllegalArgumentException(s"could not parse body: $err" )
    }
  }
}
