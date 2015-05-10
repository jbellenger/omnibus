package borg.omnibus.gtfs.util

import akka.actor.Actor

trait ActorContextExecutor {
  self: Actor =>

  protected implicit val ec = context.system.dispatcher
}
