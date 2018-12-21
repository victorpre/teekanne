package controllers
import infrastructure.api.dto.BillDto
import infrastructure.persistence.repositories.BillRepository
import javax.inject._
import models.Bill
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}


class BillController @Inject()(repo: BillRepository, cc: ControllerComponents)
                              (implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def getBill(id: Int)= Action.async { implicit request =>
    val maybeFutureBill: Future[Option[Bill]] = repo.getBillById(id)
    for {
      maybeBill: Option[Bill] <- maybeFutureBill
    } yield maybeBill match {
      case Some(bill) => Ok(Json.toJson(BillDto(bill)))
      case _ => NotFound(Json.obj("error" -> "Bill not found"))
    }
  }
}
