package borg.omnibus.providers

case class ProviderId(id: String, longName: String) {
  override def toString: String = id
}
