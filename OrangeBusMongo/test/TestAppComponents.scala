/**
  *
  */

import java.io.File

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.ApplicationLoader.Context
import play.api.{Application, ApplicationLoader, Configuration, Environment, Mode}

import scala.concurrent.ExecutionContext

trait TestAppComponents {

  lazy implicit val actorSystem = ActorSystem.create("test")
  lazy implicit val scheduler = actorSystem.scheduler
  lazy implicit val ec: ExecutionContext = play.api.libs.concurrent.Execution.defaultContext
  lazy val environment = new Environment(new File("."), ApplicationLoader.getClass.getClassLoader, Mode.Test)
  lazy val config = Configuration.load(environment).underlying
  lazy implicit val materializer = ActorMaterializer()

  lazy val context: Context = ApplicationLoader.createContext(environment)

  lazy val appLoader: AppLoader = new AppLoader()
  lazy val components: AppComponents = new AppComponents(context)
  lazy val application: Application = appLoader.load(context)
  lazy val authCache = components.cacheApi("auth-cache-test")
}
