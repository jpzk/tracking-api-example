package com.madewithtea.tracking.services

import javax.inject.{Inject, Singleton}
import com.madewithtea.tracking.requests.EventDataRequest
import com.madewithtea.tracking.sinks.CSVFileWriter
import com.twitter.finagle.Service
import com.twitter.inject.Logging
import com.twitter.util.Future

import scala.util.{Failure, Success, Try}

case class WarehouseResponse()

@Singleton
class WarehouseService @Inject()(client: CSVFileWriter) extends
  Service[EventDataRequest, Try[WarehouseResponse]] with Logging {

  override def apply(r: EventDataRequest): Future[Try[WarehouseResponse]] = {
    // Optional: Also pass the CPU values to other services
    val outcome = client.addValues(r) match {
      case Failure(e: Throwable) => {
        Failure(new CouldNotWriteValues())
      }
      case Success(clientResponse) =>
        Success(WarehouseResponse())
    }
    Future.value(outcome)
  }
}
