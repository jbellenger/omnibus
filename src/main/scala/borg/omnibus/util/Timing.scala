package borg.omnibus.util

import grizzled.slf4j.Logging

trait Timing {
  self: TimingReporter =>

  def time[T](label: String)(fn: => T): T = {
    val start = System.currentTimeMillis()
    val t = fn
    val delta = System.currentTimeMillis() - start
    report(s"$label: $delta")
    t
  }
}

trait TimingReporter {
  def report(msg: String): Unit
}

trait PrintTiming extends Timing with TimingReporter {
  override def report(msg: String): Unit = println(msg)
}

trait LogTiming extends Timing with TimingReporter with Logging {
  override def report(msg: String): Unit = info(msg)
}
