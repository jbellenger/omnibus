package borg.omnibus.csv

import borg.omnibus.gtfs._
import org.scalatest._

case class TestObj(a: String, b: String, c: Option[Long])
object TestModel extends Model[TestObj] {
  val A = required("a")
  val B = required("b")
  val C = optional("c")

  override def apply(values: Values): TestObj = TestObj(values(A), values(B), values(C))
}

class ReaderSpec extends WordSpec with Matchers {
  "A Reader" when {
    "input and model have different ordering of fields" should {
      "read lines in header order" in {
        val x = new Reader(TestModel, Seq("b","a")).map(Seq("B","A"))
        x should be (TestObj("A", "B", None))
      }
    }

    "header does not declare optional fields" should {
      "parse values as None" in {
        val x = new Reader(TestModel, Seq("a","b")).map(Seq("A","B"))
        x.c shouldBe empty
      }
    }

    "header declares optional fields but rows are missing values" should {
      "parse values as None" in {
        val x = new Reader(TestModel, Seq("a","b","c")).map(Seq("A","B"))
        x.c shouldBe empty
      }
    }

    "header declares required fields but rows are missing values" should {
      "parse values as empty string" in {
        val x = new Reader(TestModel, Seq("a","b")).map(Seq("A",""))
        x should be (TestObj("A", "", None))
      }
    }

    "header has optional field" should {
      "coerce values" in {
        val x = new Reader(TestModel, Seq("a","b","c")).map(Seq("A","B","13"))
        x.c shouldBe Some(13L)
      }
    }

    "header declares fields that are not defined in model" should {
      "ignore values for field" in {
        val x = new Reader(TestModel, Seq("a","b","d")).map(Seq("A","B","D"))
        x shouldBe TestObj("A", "B", None)
      }
    }
  }
}
