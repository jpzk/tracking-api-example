package com.madewithtea.tracking

import com.madewithtea.tracking.controllers.{ClientController, EventDataController}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.filters.CommonFilters
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter

object TrackingServerMain extends TrackingServer
class TrackingServer extends HttpServer {
  override protected def configureHttp(router: HttpRouter): Unit = {
    router.filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[EventDataController]
      .add[ClientController]
  }
}

