package borg.omnibus.scrape

import akka.actor.{Actor, ActorRef, Props}
import borg.omnibus.providers.{Provider, ProvidersComponent}
import borg.omnibus.store.StoresComponent
import borg.omnibus.util.ActorContextImplicits
import borg.omnibus.util.FutureImplicits._
import grizzled.slf4j.Logging

import scala.concurrent.duration._

trait ScrapeDriverComponent {
  self: ProvidersComponent
    with ScraperComponent
    with StoresComponent =>

  def scrapeDriver: ActorRef

  class ScrapeDriver extends Actor with ActorContextImplicits with Logging  {
    import ScrapeDriver.Messages._

    override def preStart(): Unit = {
      providers foreach {prov =>
        self ! Scrape(prov)
      }
    }

    override def receive = {
      case m@ Scrape(prov: Provider) =>
        scraper.scrape(prov).timeout(3.seconds) map {result =>
          info(s"store tick: ${prov.id}")
          gtfsrtStore.save(result)
          context.system.scheduler.scheduleOnce(prov.gtfsrt.pollInterval, self, m)
        } recover {
          case x: Throwable =>
            error("error", x)
            context.system.scheduler.scheduleOnce(prov.gtfsrt.pollInterval, self, m)
        }
    }
  }

  object ScrapeDriver {
    val props = Props(new ScrapeDriver)

    private object Messages {
      case class Scrape(provider: Provider)
    }
  }
}
