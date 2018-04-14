package stationsecurity

import java.time.DayOfWeek._

import akka.NotUsed
import akka.stream.scaladsl.{Flow, JsonFraming, Sink, Source}
import akka.util.ByteString
import io.circe.parser._
import stationsecurity.model._

import scala.concurrent.Future

trait Aggregator {

  private def flow: Flow[ByteString, Option[GateEvent], NotUsed] =
    JsonFraming.objectScanner(200)
      .map(bs => parse(bs.utf8String)) // Convert ByteString to circe JSON
      .map {
        case Right(json) => json.as[GateEvent] // Convert the JSON to a GateEvent
        case Left(error) => Left(error) // Pass the error through to final stage
      }
      .map {
        case Right(event) => Some(event) // Return the valid GateEvent
        case Left(error) =>
          system.log.error(error.getMessage) // Log the error
          None // Ignore the invalid data
      }

  private def sink: Sink[Option[GateEvent], Future[List[GateEvent]]] =
    Sink.fold(List.empty[GateEvent]) { (a, b) =>
      b match {
        case Some(ge) => a :+ ge
        case None => a
      }
    }

  /*
   * Store the data in an in-memory list of events.
   */
  def store(src: Source[ByteString, _]): Future[List[GateEvent]] = src.via(flow).runWith(sink)

}

object FridayAggregator extends Aggregator {

  type GateId = Int

  /*
   * Returns the three most used gates for entry to the station.
   */
  def getTopThreeGates(events: List[GateEvent]): List[(GateId, Int)] = {
    val out: Map[GateId, Int] = events
      .filter(e => e.status == In && e.time.getDayOfWeek == FRIDAY) // only entry event on Friday
      .groupBy(_.gate_id).mapValues(_.length)
    out.toList.sortWith(_._2 > _._2).take(3) // Get the three most popular entry gates
  }
}
