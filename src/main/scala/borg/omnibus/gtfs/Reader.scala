package borg.omnibus.gtfs

import borg.omnibus.util.BOMFilter

import scala.annotation.tailrec
import scala.io.Source

class Reader[T](model: Model[T], header: String) {
  val headerArray = header.split(",").map(_.trim)
  val headerSet = headerArray.toSet

  val missingRequiredFields = model.fields.values collect {
    case RequiredField(key) if !headerSet(key) => key
  }
  require(missingRequiredFields.isEmpty, s"missing required fields: ${missingRequiredFields.mkString(",")}")

  def map(line: String): T = {
    val values = Reader.split(line).map(_.trim())
    val pairs = headerArray.zip(values) flatMap {
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
  def apply[T](src: Source, model: Model[T]): Iterable[T] = {
    val filtered = Source.fromIterable(src.filter(BOMFilter).toIterable)
    val lines = filtered.getLines().toList

    val reader = new Reader(model, lines.head)
    lines.tail.filter(_.nonEmpty).map(reader.map)
  }

  def split(string: String): Array[String] = {
    def toString(word: List[Char]) = word.reverse.mkString

    @tailrec
    def loop(chars: List[Char], quoted: Boolean, word: List[Char], words: List[String]): List[String] = {
      chars.headOption match {
        case Some(c) if c == '"' && !quoted  =>
          loop(chars.tail, quoted=true, word, words)
        case Some(c) if c == '"' && quoted =>
          loop(chars.tail, quoted=false, word, words)
        case Some(c) if c == ',' && quoted =>
          loop(chars.tail, quoted, c :: word, words)
        case Some(c) if c == ',' && !quoted =>
          loop(chars.tail, quoted, Nil, toString(word) :: words)
        case Some(c) =>
          loop(chars.tail, quoted, c :: word, words)
        case None =>
          (toString(word) :: words).reverse
      }
    }
    loop(string.toCharArray.toList, quoted=false, Nil, Nil).toArray
  }
}
