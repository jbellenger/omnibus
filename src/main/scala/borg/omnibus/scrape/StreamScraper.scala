package borg.omnibus.scrape

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.engine.client.ClientConnectionSettings
import akka.http.model._
import akka.http.unmarshalling.Unmarshal
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString
import borg.omnibus.gtfsrt.{Record, RecordHeader}
import com.google.transit.realtime.GtfsRealtime.FeedMessage

import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}
import scala.util._

class StreamScraper(implicit system: ActorSystem) extends Scraper {
  import StreamScraper._

  val http = Http(system)
  implicit val mat = ActorFlowMaterializer()
  implicit val ec = system.dispatcher

  private def httpClient(uri: Uri): Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = {
    val auth = uri.authority
    val settings = ClientConnectionSettings(None).copy(connectingTimeout = 1.second, idleTimeout = 1.second)
    http.outgoingConnection(auth.host.address(), uri.effectivePort, settings = Some(settings))
  }

  private val collectChunks = Flow[HttpResponse].mapAsync {resp =>
    Unmarshal(resp).to[ByteString].map(bs => (resp, bs))
  }

  private val parseProto = Flow[(HttpResponse, ByteString)].map {
    case (resp, bs) =>
      if (resp.status == StatusCodes.OK) {
        Try(FeedMessage.parseFrom(bs.toArray))
      } else {
        Failure(RemoteResponseException(resp))
      }
  }

  override def scrape(s: Scrapable): Future[Iterable[Record]] = {
    val futs = s.gtfsrt.apis.groupBy(_.uri) map {
      case (uri, apis) =>
        val req = HttpRequest(uri = uri.toRelative)

        val promise = Promise[Iterable[Record]]()

        val replySink = Sink.foreach[Try[FeedMessage]] {result =>
          val records = result map {proto =>
            val header = RecordHeader(s.id, proto.getHeader)
            apis.flatMap(x => x.parse(header, proto))
          }

          promise.complete(records)
        }

        Source.single(req)
          .via(httpClient(uri))
          .via(collectChunks)
          .via(parseProto)
          .runWith(replySink)

        promise.future
    }

    Future.fold(futs)(List.empty[Record])(_ ++ _)
  }

  override def shutdown(): Unit = {}
}

object StreamScraper {
  sealed trait ScrapeException extends Exception

  case class RemoteResponseException(resp: HttpResponse) extends ScrapeException
}
