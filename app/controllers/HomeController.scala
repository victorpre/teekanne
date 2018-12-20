package controllers

import infrastructure.persistence.repositories.{BillRepository}
import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext}

class HomeController @Inject()(repo: BillRepository, cc: ControllerComponents)
                              (implicit ec: ExecutionContext)
  extends AbstractController(cc) {


  def appSummary = Action.async { implicit request =>

    val futureBill = repo.findById(1)
    futureBill.map { bill =>
      Ok(Json.obj("content" -> s"Scala Play React Seed ${bill.text} with ${bill.price.toString()} at ${bill.purchaseDate.toString}"))
    }
  }
}
