package borg.omnibus.util

import akka.actor.Actor

trait ActorContextExecutor {
  self: Actor =>

  protected implicit val ec = context.system.dispatcher
}
