package borg.omnibus.scrape

import akka.actor.{Actor, ActorRef, Props}
import borg.omnibus.gtfsrt.{AlertRecord, TripUpdateRecord, VehiclePositionRecord}
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
        val fut = scraper.scrape(prov).timeout(3.seconds) map {records =>
          info(s"store tick provider=${prov.id} records=${records.size}")
          records foreach {
            case r: TripUpdateRecord => tripUpdatesStore.save(r)
            case r: AlertRecord => alertStore.save(r)
            case r: VehiclePositionRecord => vehiclePositionStore.save(r)
          }
        } recover {
          case x: Throwable =>
            error("error", x)
        }
        fut.onComplete {_ =>
          context.system.scheduler.scheduleOnce(prov.gtfsrt.interval, self, Scrape(prov))
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
