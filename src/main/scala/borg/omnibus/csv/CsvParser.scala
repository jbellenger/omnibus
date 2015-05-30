package borg.omnibus.csv

import org.parboiled2._

// taken from the parboiled2 csv parser
// https://github.com/sirthias/parboiled2/blob/release-2.1/examples/src/main/scala/org/parboiled2/examples/CsvParser.scala

object CsvParser extends {

  /**
   * Parses the given input into a [[CsvFile]] or an [[Error]] instance.
   */
  def apply(input: ParserInput, headerPresent: Boolean = true, fieldDelimiter: Char = ','): Either[Error, CsvFile] = {
    import Parser.DeliveryScheme.Either
    val parser = new CsvParser(input, headerPresent, fieldDelimiter)
    parser.file.run().left.map(error => Error(parser.formatError(error)))
  }

  private val `TEXTDATA-BASE` = CharPredicate.Printable -- '"'
  private val QTEXTDATA = `TEXTDATA-BASE` ++ "\r\n"
}

class CsvParser(val input: ParserInput, headerPresent: Boolean, fieldDelimiter: Char) extends Parser with StringBuilding {
  import CsvParser._

  val TEXTDATA = `TEXTDATA-BASE` -- fieldDelimiter
  def BOM = '\uFEFF'

  def file = rule {
    OBOM ~ OWS ~ optional(test(headerPresent) ~ header ~ NL) ~ oneOrMore(record).separatedBy(NL) ~ optional(NL) ~ EOI ~> CsvFile
  }

  def header = rule { record }

  def record = rule { oneOrMore(field).separatedBy(fieldDelimiter) ~> Record }

  def field = rule { `quoted-field` | `unquoted-field` }

  def `quoted-field` = rule {
    OWS ~ '"' ~ clearSB() ~ zeroOrMore((QTEXTDATA | "\"\"") ~ appendSB()) ~ '"' ~ OWS ~ push(sb.toString)
  }

  def `unquoted-field` = rule { capture(zeroOrMore(TEXTDATA)) }

  def NL = rule { optional('\r') ~ '\n' }

  def OWS = rule { zeroOrMore(' ') }
  def OBOM = rule {
    optional('\uFEFF')
  }
}
