package stationsecurity.model

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}

sealed trait Status {
  def name: String
  override def toString: String = name
}

object Status {
  implicit val decodeStatus: Decoder[Status] = new Decoder[Status] {
    override def apply(c: HCursor): Result[Status] = {
      for {
        status <- c.value.as[String]
      } yield status.toLowerCase match {
        case "in" => In
        case "out" => Out
      }
    }
  }

  implicit val encodeStatus: Encoder[Status] = new Encoder[Status] {
    override def apply(a: Status): Json = Json.fromString(a.name)
  }
}

object In extends Status {
  def name: String = "in"
}

object Out extends Status {
  def name: String = "out"
}
