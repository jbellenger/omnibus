package borg.omnibus.gtfs2

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
        val x = new Reader(TestModel, "b,a").map("B,A")
        x should be (TestObj("A", "B", None))
      }
    }
    "header has whitespace" should {
      "trim whitespace" in {
        val x = new Reader(TestModel, "  a  , b ").map("A,B")
        x should be (TestObj("A", "B", None))
      }
    }

    "map input has whitespace" should {
      "trim whitespace" in {
        val x = new Reader(TestModel, "a,b").map(" A , B ")
        x should be (TestObj("A", "B", None))
      }
    }

    "header does not declare optional fields" should {
      "parse values as None" in {
        val x = new Reader(TestModel, "a,b").map("A,B")
        x.c shouldBe empty
      }
    }

    "header declares optional fields but rows are missing values" should {
      "parse values as None" in {
        val x = new Reader(TestModel, "a,b,c").map("A,B")
        x.c shouldBe empty
      }
    }

    "header has optional field" should {
      "coerce values" in {
        val x = new Reader(TestModel, "a,b,c").map("A,B,13")
        x.c shouldBe Some(13L)
      }
    }
  }
}
