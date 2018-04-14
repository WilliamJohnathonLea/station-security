package stationsecurity

import java.nio.file.Paths

import akka.stream.scaladsl.FileIO

object Main extends App {

  val filePath = Paths.get("samples/sample.json")

  FridayAggregator.store(FileIO.fromPath(filePath))
    .map(FridayAggregator.getTopThreeGates)
    .foreach { events =>
      println("### Top 3 Entry gates on Friday ###")
      events.foreach { e =>
        println(s"Gate ${e._1} had ${e._2} people enter")
      }
      system.terminate()
    }

}
