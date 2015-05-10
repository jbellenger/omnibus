package borg.omnibus.gtfs.scrape

import akka.actor.{Actor, ActorRef, Props}
import borg.omnibus.gtfs.providers.{Provider, ProvidersComponent}
import borg.omnibus.gtfs.store.StoreComponent
import borg.omnibus.gtfs.util.ActorContextExecutor

trait ScrapeDriverComponent {
  self: ProvidersComponent
    with ScraperComponent
    with StoreComponent =>

  def scrapeDriver: ActorRef

  class ScrapeDriver extends Actor with ActorContextExecutor {
    import ScrapeDriver.Messages._

    override def preStart(): Unit = {
      providers foreach {prov =>
        self ! Scrape(prov)
      }
    }

    override def receive = {
      case m@ Scrape(prov: Provider) =>
        scraper.scrape(prov) map {result =>
          store.save(result)
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
