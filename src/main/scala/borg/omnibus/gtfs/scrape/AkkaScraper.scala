package borg.omnibus.gtfs.scrape

import akka.actor._
import akka.http.Http
import akka.http.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.pattern.ask
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.util.Timeout
import borg.omnibus.gtfs.providers.Provider
import borg.omnibus.gtfs.scrape.Scraper.ScrapeResult
import com.google.transit.realtime.GtfsRealtime.FeedMessage

import scala.collection.JavaConversions._
import scala.concurrent.Future
import scala.concurrent.duration._

class AkkaScraper(implicit arf: ActorRefFactory) extends Scraper {
  import AkkaScraperActor.Messages._

  private implicit val timeout: Timeout = 3.seconds
  private implicit val ec = arf.dispatcher
  private var ref = arf.actorOf(AkkaScraperActor.props)

  private def withRef[T](fn: => T): T = {
    require(ref != null, "method called after scraper has been shutdown")
    fn
  }

  override def scrape(provider: Provider): Future[ScrapeResult] = withRef {
    (ref ? ScrapeReq(provider)).mapTo[ScrapeRep].map(_.result)
  }

  override def shutdown(): Unit = {
    ref ! PoisonPill
    ref = null
  }

  // JMB TODO: this should be doable without actors using only streams
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

        val printChunksConsumer = Sink.foreach[HttpResponse] { res =>
          if (res.status == StatusCodes.OK) {
            res.entity.getDataBytes().map { chunk =>
              val msg = FeedMessage.parseFrom(chunk.toArray)
              msg.getEntityList foreach {ent =>
                println(ent)
              }
            }.to(Sink.ignore()).run()
          } else {
            println(res.status)
          }
        }

        val finishFuture = Source.single(req).via(httpClient).runWith(printChunksConsumer)
        osender ! ScrapeRep(result = true)
    }
  }

  private object AkkaScraperActor {
    def props = Props(new AkkaScraperActor)

    object Messages {
      case class ScrapeReq(provider: Provider)
      case class ScrapeRep(result: ScrapeResult)
    }
  }
}
