package com.madewithtea.tracking.sinks

import java.io._
import javax.inject.Singleton

import com.github.tototoshi.csv.CSVWriter
import com.madewithtea.tracking.Config
import com.madewithtea.tracking.requests.EventDataRequest
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.util.{Failure, Success, Try}

case class CSVWriterResponse()

@Singleton
class CSVFileWriter {
  val na = "N/A"

  /**
    * Adds the values to the CSV file of given hour, if raised exceptions
    * sends back Failure, this will result into resending on the client side
    *
    * @param r
    * @return
    */
  def addValues(r: EventDataRequest): Try[CSVWriterResponse] = {
    try {
      val fmt = DateTimeFormat.forPattern("yyyyMMdd")
      val time = DateTime.now().toString(fmt)
      val dir = Config.CSVDirectory
      val writer = CSVWriter.open(new File(s"$dir/events-$time.csv"), append = true)
      writer.writeRow(Seq(r.time,
        r.site.getOrElse(na),
        r.version.getOrElse(na),
        r.remoteAdress,
        r.userAgent.getOrElse(na),
        r.cookie.getOrElse(na),
        r.fingerprint.getOrElse(na),
        r.screen.getOrElse(na),
        r.referer.getOrElse(na),
        r.event.getOrElse(na)))
      writer.close()
    } catch {
      case t: Throwable =>
        return Failure(t)
    }
    Success(CSVWriterResponse())
  }
}
