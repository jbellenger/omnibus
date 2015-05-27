package borg.omnibus.gtfs

import java.net.URL

case class FeedInfo(
  feedPublisherName: String,
  feedPublisherUrl: URL,
  feedLang: String,
  feedStartDate: Option[String],
  feedEndDate: Option[String],
  feedVersion: Option[String])

object FeedInfoModel extends Model[FeedInfo] {
  val FeedPublisherName = required("feed_publisher_name")
  val FeedPublisherUrl = required("feed_publisher_url")
  val FeedLang = required("feed_lang")
  val FeedStartDate = optional("feed_start_date")
  val FeedEndDate = optional("feed_end_date")
  val FeedVersion = optional("feed_version")

  override def apply(values: Values) =
    FeedInfo(
      values(FeedPublisherName),
      values(FeedPublisherUrl),
      values(FeedLang),
      values(FeedStartDate),
      values(FeedEndDate),
      values(FeedVersion))
}
