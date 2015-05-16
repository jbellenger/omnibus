package borg.omnibus.scrape

import akka.actor._
import akka.http.Http
import akka.http.engine.client.ClientConnectionSettings
import akka.http.model._
import akka.http.unmarshalling.Unmarshal
import akka.pattern.ask
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.{ByteString, Timeout}
import borg.omnibus.gtfsrt.GtfsrtSnapshot
import borg.omnibus.providers.Provider
import com.google.transit.realtime.GtfsRealtime.FeedMessage
import grizzled.slf4j.Logging

import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

class AkkaScraper(implicit arf: ActorRefFactory) extends Scraper with Logging {
  import AkkaScraperActor.Messages._

  private implicit val timeout: Timeout = 3.seconds
  private implicit val ec = arf.dispatcher
  private var _ref = arf.actorOf(AkkaScraperActor.props)

  private def ref: ActorRef = {
    require(_ref != null, "method called after scraper has been shutdown")
    _ref
  }

  override def scrape(provider: Provider): Future[GtfsrtSnapshot] = {
    val p = Promise[GtfsrtSnapshot]()
    (ref ? ScrapeReq(provider)).mapTo[ScrapeRep].map {
      case ScrapeRep(result) => p.complete(result)
    }
    p.future
  }

  override def shutdown(): Unit = {
    ref ! PoisonPill
    _ref = null
  }

  private class AkkaScraperActor extends Actor {
    val http = Http(context.system)
    implicit val materializer = ActorFlowMaterializer()

    def httpClient(uri: Uri): Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = {
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
          Failure(new AkkaScraperException(resp))
        }
    }

    override def receive: Receive = {
      case ScrapeReq(provider) =>
        val osender = sender()

        val uri = provider.gtfsrt.uri
        val req = HttpRequest(uri = uri.toRelative)

        val replySink = Sink.foreach[Try[FeedMessage]] {
          case Success(proto) =>
            val parsed = GtfsrtSnapshot(provider, proto)
            osender ! ScrapeRep(Success(parsed))
          case Failure(err) =>
            osender ! ScrapeRep(Failure(err))
        }

        Source.single(req)
          .via(httpClient(uri))
          .via(collectChunks)
          .via(parseProto)
          .runWith(replySink)
    }
  }

  private object AkkaScraperActor {
    def props = Props(new AkkaScraperActor)

    object Messages {
      case class ScrapeReq(provider: Provider)
      case class ScrapeRep(result: Try[GtfsrtSnapshot])
    }
  }

  class AkkaScraperException(response: HttpResponse) extends Exception
}
