package borg.omnibus.csv

case class CsvFile(header: Option[Record], records: Seq[Record])
