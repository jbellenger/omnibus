package borg.omnibus.util

import akka.actor.Actor

trait ActorContextSystem {
  self: Actor =>

  implicit val sys = context.system
}
