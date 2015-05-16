package borg.omnibus.scrape

import akka.actor.{Actor, ActorRef, Props}
import borg.omnibus.providers.{Provider, ProvidersComponent}
import borg.omnibus.store.StoresComponent
import borg.omnibus.util.ActorContextExecutor
import grizzled.slf4j.Logging

trait ScrapeDriverComponent {
  self: ProvidersComponent
    with ScraperComponent
    with StoresComponent =>

  def scrapeDriver: ActorRef

  class ScrapeDriver extends Actor with ActorContextExecutor with Logging {
    import ScrapeDriver.Messages._

    override def preStart(): Unit = {
      providers foreach {prov =>
        self ! Scrape(prov)
      }
    }

    override def receive = {
      case m@ Scrape(prov: Provider) =>
        scraper.scrape(prov) map {result =>
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
