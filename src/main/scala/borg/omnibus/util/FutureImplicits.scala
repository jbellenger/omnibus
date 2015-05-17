package borg.omnibus.util

import java.util.concurrent.TimeoutException

import akka.actor.ActorSystem

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{Future, Promise}

object FutureImplicits {
  implicit class RichFuture[T](fut: Future[T]) {
    /**
     * Buyer beware: this method does not cancel the original future.
     */
    def timeout(dur: FiniteDuration)(implicit sys: ActorSystem): Future[T] = {
      implicit val ec = sys.dispatcher

      val guard = Promise[T]()
      val cancel = sys.scheduler.scheduleOnce(dur) {
        guard.failure(new TimeoutException)
      }
      fut onComplete {
        case _ => cancel.cancel()
      }
      Future.firstCompletedOf(List(fut, guard.future))(sys.dispatcher)
    }
  }
}
