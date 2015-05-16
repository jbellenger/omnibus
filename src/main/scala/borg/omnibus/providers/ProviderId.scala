package borg.omnibus.providers

case class ProviderId(name: String) extends AnyVal {
  override def toString = name
}
