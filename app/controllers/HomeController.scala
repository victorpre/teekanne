package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

class HomeController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) {

  implicit private val ec = cc.executionContext

  def appSummary = Action {

       Ok(Json.obj("content" -> s"Scala Play React Seed"))
  }
}
