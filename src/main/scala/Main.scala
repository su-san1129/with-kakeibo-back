package work.withkakeibo

import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.model._
import org.apache.pekko.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContextExecutor

object Main extends App {
  implicit val system: ActorSystem[Nothing] =
    ActorSystem(Behaviors.empty, "with-kakeibo-system")
  implicit val executionContext: ExecutionContextExecutor =
    system.executionContext

  val route =
    path("hello") {
      get {
        complete(
          HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            "<h1>Say hello to Pekko HTTP</h1>"
          )
        )
      }
    }

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

  sys.addShutdownHook {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
