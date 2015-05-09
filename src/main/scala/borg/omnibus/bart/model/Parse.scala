package borg.omnibus.bart.model

private[model] object Parse {
  def parse(text: String): List[Array[String]] = text.lines.toList.map(_.split(','))
}
