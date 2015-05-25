package borg.omnibus.gtfs2

import java.net.URL

case class Agency(
  agencyId: Option[String],
  agencyName: String,
  agencyUrl: URL,
  agencyTimezone: String,
  agencyLang: Option[String],
  agencyPhone: Option[String],
  agencyFareUrl: Option[URL])


object AgencyModel extends Model[Agency] {
  val AgencyId = optional("agency_id")
  val AgencyName = required("agency_name")
  val AgencyUrl = required("agency_url")
  val AgencyTimezone = required("agency_timezone")
  val AgencyLang = optional("agency_lang")
  val AgencyPhone = optional("agency_phone")
  val AgencyFareUrl = optional("agency_fare_url")

  override def apply(values: Values): Agency = {
    Agency(
      values(AgencyId),
      values(AgencyName),
      values(AgencyUrl),
      values(AgencyTimezone),
      values(AgencyLang),
      values(AgencyPhone),
      values(AgencyFareUrl))
  }
}
