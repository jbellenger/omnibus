package borg.omnibus.util

import akka.actor.Actor

trait ActorContextImplicits extends ActorContextExecutor with ActorContextSystem {
  self: Actor =>
}
