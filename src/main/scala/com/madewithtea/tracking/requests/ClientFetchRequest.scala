package com.madewithtea.tracking.requests

case class ClientFetchRequest(site: Option[String], version: Option[String],
                              referer: Option[String], useragent: Option[String],
                              remote: String)
