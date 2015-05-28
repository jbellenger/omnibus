package borg.omnibus.util

object BOMFilter extends (Char => Boolean) {
  override def apply(v1: Char): Boolean = v1 match {
    case '\uFEFF' => false
    case _ => true
  }
}
