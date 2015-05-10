package borg.omnibus.scrape

import akka.actor._
import akka.http.Http
import akka.http.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.pattern.ask
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.util.Timeout
import borg.omnibus.gtfsrt.GtfsrtSnapshot
import borg.omnibus.providers.Provider
import borg.omnibus.scrape.Scraper.ScrapeResult
import com.google.transit.realtime.GtfsRealtime.FeedMessage

import scala.concurrent.Future
import scala.concurrent.duration._

class AkkaScraper(implicit arf: ActorRefFactory) extends Scraper {
  import AkkaScraperActor.Messages._

  private implicit val timeout: Timeout = 3.seconds
  private implicit val ec = arf.dispatcher
  private var _ref = arf.actorOf(AkkaScraperActor.props)

  private def ref: ActorRef = {
    require(_ref != null, "method called after scraper has been shutdown")
    _ref
  }

  override def scrape(provider: Provider): Future[ScrapeResult] = {
    (ref ? ScrapeReq(provider)).mapTo[ScrapeRep].map(_.result)
  }

  override def shutdown(): Unit = {
    ref ! PoisonPill
    _ref = null
  }

  private class AkkaScraperActor extends Actor {
    val http = Http(context.system)
    implicit val materializer = ActorFlowMaterializer()

    override def receive: Receive = {
      case ScrapeReq(provider) =>
        val osender = sender()

        val uri = provider.gtfsrt.uri
        val httpClient = {
          val auth = uri.authority
          http.outgoingConnection(auth.host.address(), uri.effectivePort)
        }

        val req = HttpRequest(uri = uri.toRelative)

        val replySink = Sink.foreach[HttpResponse] { res =>
          if (res.status == StatusCodes.OK) {
            res.entity.getDataBytes().map { chunk =>
              val proto = FeedMessage.parseFrom(chunk.toArray)
              val parsed = GtfsrtSnapshot(proto)
              osender ! ScrapeRep(parsed)
            }.to(Sink.ignore()).run()
          }
        }

        Source.single(req).via(httpClient).runWith(replySink)
    }
  }

  private object AkkaScraperActor {
    def props = Props(new AkkaScraperActor)

    object Messages {
      case class ScrapeReq(provider: Provider)
      case class ScrapeRep(result: GtfsrtSnapshot)
    }
  }
}
