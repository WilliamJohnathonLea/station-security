package stationsecurity

import java.time.{ZoneId, ZonedDateTime}

import akka.stream.scaladsl.Source
import akka.util.ByteString
import io.circe.syntax._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import stationsecurity.model._

import scala.concurrent.Await
import scala.concurrent.duration._

class FridayAggregatorSpec extends WordSpecLike with Matchers with BeforeAndAfterAll {

  "The aggregator" should {

    "Store the valid JSON data" in {

      val friday = ZonedDateTime.of(
        // Friday 13 April 2018 12:00
        2018, 4, 13, 12, 0, 0, 0, ZoneId.systemDefault()
      )

      val expected = List(
        GateEvent(1, 1, In, friday)
      )

      val validEvents = List(
        ByteString(GateEvent(1, 1, In, friday).asJson.toString())
      )

      val src = Source(validEvents :+ ByteString("""{"invalid":"json event"}"""))

      val result = Await.result(FridayAggregator.store(src), 5.seconds)

      result shouldEqual expected
    }

    "Retrieve the three most popular gates used on Friday" in {

      val friday = ZonedDateTime.of(
        // Friday 13 April 2018 12:00
        2018, 4, 13, 12, 0, 0, 0, ZoneId.systemDefault()
      )

      val expected = List(
        (1, 4),
        (2, 3),
        (3, 2)
      )

      val validEvents = List(
        GateEvent(1, 1, In, friday),
        GateEvent(2, 1, In, friday),
        GateEvent(3, 1, In, friday),

        GateEvent(1, 1, Out, friday),
        GateEvent(2, 1, Out, friday),
        GateEvent(3, 1, Out, friday),

        GateEvent(1, 1, In, friday),
        GateEvent(2, 1, In, friday),
        GateEvent(3, 1, In, friday),

        GateEvent(1, 1, In, friday),
        GateEvent(2, 1, In, friday),

        GateEvent(1, 1, In, friday),

        GateEvent(4, 1, In, friday)
      ).map(e => ByteString(e.asJson.toString()))

      val src = Source(validEvents :+ ByteString("""{"invalid":"json event"}"""))

      val result = Await.result(
        FridayAggregator.store(src).map(FridayAggregator.getTopThreeGates)
        , 5.seconds
      )

      result shouldEqual expected
    }

  }

}
