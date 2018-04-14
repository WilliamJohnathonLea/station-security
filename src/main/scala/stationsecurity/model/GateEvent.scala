package stationsecurity.model

import java.time.ZonedDateTime

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}

case class GateEvent(gate_id: Int, person_id: Int, status: Status, time: ZonedDateTime)

object GateEvent {
  implicit val decodeGateEvent: Decoder[GateEvent] = new Decoder[GateEvent] {

    private def parseDate(ds: String): ZonedDateTime = {
      ZonedDateTime.parse(ds)
    }

    override def apply(c: HCursor): Result[GateEvent] = {
      for {
        gate_id <- c.downField("gate_id").as[Int]
        person_id <- c.downField("person_id").as[Int]
        status <- c.downField("status").as[Status]
        time <- c.downField("time").as[String]
      } yield GateEvent(gate_id, person_id, status, parseDate(time))
    }
  }

  implicit val encodeGateEvent: Encoder[GateEvent] = new Encoder[GateEvent] {
    override def apply(a: GateEvent): Json = Json.obj(
      "gate_id" -> Json.fromInt(a.gate_id),
      "person_id" -> Json.fromInt(a.person_id),
      "status" -> Json.fromString(a.status.name),
      "time" -> Json.fromString(a.time.toString)
    )
  }
}
