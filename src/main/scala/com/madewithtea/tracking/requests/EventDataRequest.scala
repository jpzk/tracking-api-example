package com.madewithtea.tracking.requests

case class EventDataRequest(time: Long, site: Option[String], version: Option[String],
                            remoteAdress: String, userAgent: Option[String],
                            cookie: Option[String], fingerprint: Option[String], screen: Option[String],
                            event: Option[String], referer: Option[String])

