package borg.omnibus.csv

import org.scalatest.{Matchers, WordSpec}

class CsvParserSpec extends WordSpec with Matchers {
  "CsvParser" should {
    "correctly parse simple CSV test input" in {
      CsvParser {
        """"first_name","last_name","company_name","address","city","county","state","zip","phone1","phone2","email","web"
          |"James","Butt", "Benton, John B Jr","6649 N Blue Gum St","New Orleans","Orleans","LA",70116,"504-621-8927","504-845-1427","jbutt@gmail.com","http://www.bentonjohnbjr.com"
          |"Josephine","Darakjy","Chanay, Jeffrey A Esq","4 B Blue Ridge Blvd","Brighton","Livingston","MI",48116,"810-292-9388","810-374-9840","josephine_darakjy@darakjy.org","http://www.chanayjeffreyaesq.com"
          |Art,"Venere","Chemel, James L Cpa","8 W Cerritos Ave #54","Bridgeport","Gloucester","NJ",08014 ,"856-636-8749","856-264-4130","art@venere.org","http://www.chemeljameslcpa.com"
          |"Lenna","Paprocki","Feltz ""Printing"" Service", 639 Main St,"Anchorage","Anchorage","AK",99501,"907-385-4412","907-921-2010","lpaprocki@hotmail.com","http://www.feltzprintingservice.com"
          |""".stripMargin
      } shouldBe file(
        record("first_name","last_name","company_name","address","city","county","state","zip","phone1","phone2","email","web"),
        record("James","Butt", "Benton, John B Jr","6649 N Blue Gum St","New Orleans","Orleans","LA","70116","504-621-8927","504-845-1427","jbutt@gmail.com","http://www.bentonjohnbjr.com"),
        record("Josephine","Darakjy","Chanay, Jeffrey A Esq","4 B Blue Ridge Blvd","Brighton","Livingston","MI","48116","810-292-9388","810-374-9840","josephine_darakjy@darakjy.org","http://www.chanayjeffreyaesq.com"),
        record("Art","Venere","Chemel, James L Cpa","8 W Cerritos Ave #54","Bridgeport","Gloucester","NJ","08014 ","856-636-8749","856-264-4130","art@venere.org","http://www.chemeljameslcpa.com"),
        record("Lenna","Paprocki","Feltz \"Printing\" Service"," 639 Main St","Anchorage","Anchorage","AK","99501","907-385-4412","907-921-2010","lpaprocki@hotmail.com","http://www.feltzprintingservice.com"))
    }

    "handle quoted commas" in {
      CsvParser {
        """
          |a,b
          |"A,A",b
        """.trim().stripMargin
      } shouldBe file (
        record("a", "b"),
        record("A,A", "b")
      )
    }

    "ignore byte order marks" in {
      val body =
        "\uFEFF\"a\",\"b\"\r\n" +
        "\"A\",\"B\"\r\n"

      CsvParser(body) shouldBe file(
        record("a", "b"),
        record("A", "B")
      )
    }

    "ignore multiple trailing new lines" in {
      val body =
        """"a","b"
          |A,B
          |A,B
          |
          |
          |
          |""".stripMargin
      CsvParser(body) shouldBe file(
        record("a", "b"),
        record("A", "B"),
        record("A", "B")
      )
    }

    "ignore missing records" in {
      val body = "a,b\n"
      CsvParser(body) shouldBe file(record("a", "b"))
    }
  }

  def file(header: Record, records: Record*) = Right(CsvFile(Some(header), records.toVector))
  def record(fields: String*) = Record(fields.toVector)
}

