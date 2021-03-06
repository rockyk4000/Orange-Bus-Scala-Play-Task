import controllers.{Assets, AsyncController, CountController, HomeController, UserFormController}
import play.api.ApplicationLoader.Context
import play.api.cache.EhCacheComponents
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, Configuration, LoggerConfigurator}
import play.modules.reactivemongo.{MongoController, ReactiveMongoComponents, ReactiveMongoApi, DefaultReactiveMongoApi}
import scala.concurrent.ExecutionContext
import router.Routes

class AppLoader extends ApplicationLoader {

  implicit val ec: ExecutionContext = play.api.libs.concurrent.Execution.defaultContext

  override def load(context: Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment)
    }


    val components: AppComponents = new AppComponents(context)

    components.application
  }

}

class AppComponents(context: Context)(implicit val ec: ExecutionContext) extends BuiltInComponentsFromContext(context)
  with EhCacheComponents {

  val config = context.initialConfiguration.underlying

  implicit val scheduler = actorSystem.scheduler



  lazy val homeController = new HomeController
  lazy val countController = new CountController(services.AtomicCounter)
  lazy val asyncController = new AsyncController(actorSystem)
  val reactiveMongoApi = new DefaultReactiveMongoApi(context.initialConfiguration, applicationLifecycle)
  lazy val mongoController = new UserFormController(reactiveMongoApi)

  lazy val assetsController: Assets = new Assets(httpErrorHandler)

  // order matters - should be the same as routes file
  lazy val router = new Routes(
    httpErrorHandler,
    homeController,
    countController,
    asyncController,
    mongoController,
    assetsController)

}

