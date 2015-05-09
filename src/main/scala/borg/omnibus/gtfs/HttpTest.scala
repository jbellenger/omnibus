package borg.omnibus.gtfs

import java.net.InetSocketAddress

import akka.actor._
import akka.http.Http
import akka.http.model._
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl._
import borg.omnibus.gtfs.model.Models

import scala.concurrent.Await
import scala.concurrent.duration._
import com.google.transit.realtime.GtfsRealtime._
import scala.collection.JavaConversions._

object HttpTest {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("http-test")
    val models = Models.fromResource("/bart-gtfs.zip")

    // val host = "james.bellenger.org"
    val host = "api.bart.gov"
    // http://api.bart.gov/gtfsrt/tripupdate.aspx
    val httpClient = Http(system).outgoingConnection(host, 80)

    implicit val materializer = ActorFlowMaterializer()

    val req = HttpRequest(uri = Uri("/gtfsrt/tripupdate.aspx"))

    val printChunksConsumer = Sink.foreach[HttpResponse] { res =>
      if (res.status == StatusCodes.OK) {
        res.entity.getDataBytes().map { chunk =>
          val msg = FeedMessage.parseFrom(chunk.toArray)
          msg.getEntityList foreach {ent =>
            println(ent)
          }
        }.to(Sink.ignore).run()
      } else {
        println(res.status)
      }
    }

    val finishFuture = Source.single(req).via(httpClient).runWith(printChunksConsumer)

    Await.result(finishFuture, 3.seconds)
    system.shutdown()
    system.awaitTermination()
  }
}
