package borg.omnibus.store

trait Codec[A, B] {
  def encode(a: A): B
  def decode(b: B): A
}
