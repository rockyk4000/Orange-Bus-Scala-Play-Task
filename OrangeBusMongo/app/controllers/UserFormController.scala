package controllers

import play.api.mvc.{Action,Controller}
import scala.concurrent.Future
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import models.UserInfo
import play.modules.reactivemongo.{MongoController, ReactiveMongoComponents, ReactiveMongoApi}
import reactivemongo.play.json._
import play.modules.reactivemongo.json.collection._
import models.UserInfo.userInfoJson
import scala.concurrent.ExecutionContext

class UserFormController(val reactiveMongoApi: ReactiveMongoApi)(implicit ec: ExecutionContext) extends Controller  with MongoController with ReactiveMongoComponents
{
     def mongoCollection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("userInfo"))
     
    val userForm = Form(
        mapping(
            "name" -> nonEmptyText,
            "email" -> nonEmptyText
        )(UserInfo.apply)(UserInfo.unapply) 
    )
    
    /** Serve the form page */
    def get = Action.async{
        implicit request =>
        val emptyForm = userForm
        Future.successful(
                Ok(views.html.mongoform(userForm)
                ))

    }
    
    /** Handle post request */
    def post = Action.async{
        implicit request =>
        userForm.bindFromRequest().fold(
            
            //error
            formWithErrors=>{
                Logger.error("form has errors")
        
                Future.successful(BadRequest(views.html.mongoformError(userForm)))
            },
             //bind was done
            user =>{
            
                val futureResult = mongoCollection.flatMap(_.insert(user))
                futureResult.map(r=>Ok(r.message))
                Future.successful(Ok("User Upload Success"))
            }
        )
    }
    
}