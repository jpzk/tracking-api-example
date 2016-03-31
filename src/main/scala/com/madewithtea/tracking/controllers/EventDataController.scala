package com.madewithtea.tracking.controllers

import javax.inject.{Inject, Singleton}
import com.madewithtea.tracking.requests.EventDataRequest
import com.madewithtea.tracking.services.{CouldNotWriteValues, WarehouseService}
import com.madewithtea.tracking.sinks.{CSVFileWriter, InfluxDBClient}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging
import org.joda.time.DateTime
import scala.util.{Failure, Success}

@Singleton
class EventDataController @Inject()(client: CSVFileWriter)
  extends Controller with Logging {

  val warehouseService = new WarehouseService(client)

  get("/h", name = "pixel_endpoint") { request: Request =>
    track(request)
  }

  /**
    * Convert GET parameters to EventDataRequest object
    *
    * @param request
    * @return
    */
  def deserialize(request: Request): EventDataRequest = {
    val ip = request.remoteAddress.toString
    val ua = request.userAgent
    val site = request.params.get("s")
    val siteversion = request.params.get("v")
    val cookie = request.params.get("u")
    val fingerprint = request.params.get("fp")
    val screen = request.params.get("s")
    val event = request.params.get("e")
    val time = DateTime.now().getMillis
    val referer = request.referer

    EventDataRequest(time, site, siteversion,
      ip, ua, cookie, fingerprint, screen, event, referer)
  }

  def track(request: Request) = {
    val eventDataRequest = deserialize(request)
    InfluxDBClient.writeEventData(eventDataRequest)
    warehouseService(eventDataRequest) flatMap { promise =>
      promise match {
        case Success(result) => {
          response.ok.body("").toFuture
        }
        case Failure(e: CouldNotWriteValues) =>
          warn("Could not write values; ask client for resending")
          response.internalServerError.toFuture
        case Failure(e) =>
          warn("Some unexpected error when writing values")
          response.internalServerError.toFuture
      }
    }
  }


}

