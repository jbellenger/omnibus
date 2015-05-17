package borg.omnibus.util

import java.util.concurrent.TimeoutException

import akka.actor.ActorSystem
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.Span._
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future
import scala.concurrent.duration.{FiniteDuration, _}

class FutureImplicitsSpec extends WordSpec with Matchers with ScalaFutures {

  import FutureImplicits._
  implicit val sys = ActorSystem("future-implicits-spec")
  implicit val ec = sys.dispatcher

  val defaultDuration = 100.millis

  def TestFuture[T](t: T, when: FiniteDuration = defaultDuration): Future[T] = {
    Future {
      Thread.sleep(when.toMillis)
      t
    }
  }

  def whenReady[T, U](fut: Future[T])(fun: T => U): U = {
    whenReady(fut, Timeout(defaultDuration))(fun)
  }

  "A RichFuture" when {
    "timeout" should {
      "throw TimeoutException on timeout" in {
        val fut = TestFuture(1, 3.seconds).timeout(defaultDuration - 50.millis)
        whenReady(fut.failed) {
          _ shouldBe a [TimeoutException]
        }
      }
      "not throw TimeoutException if the main future completes" in {
        val fut = TestFuture(1).timeout(2.seconds)
        whenReady(fut) {
          _ shouldBe 1
        }
      }
      "not throw TimeoutException for already-completed futures" in {
        whenReady(Future(1).timeout(defaultDuration)) {
          _ shouldBe 1
        }
      }
      "not throw TimeoutException for already-failed futures" in {
        object TestException extends Exception
        val fut = Future.failed(TestException).timeout(defaultDuration)

        whenReady(fut.failed) {
          _ shouldBe TestException
        }
      }
    }
  }

  "A TestFuture" should {
    "complete" in {
      val f = convertScalaFuture(TestFuture(1))
      whenReady(f) { x =>
        x shouldBe 1
      }
    }
  }
}
