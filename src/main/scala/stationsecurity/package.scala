import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

package object stationsecurity {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val ec = system.dispatcher

}
