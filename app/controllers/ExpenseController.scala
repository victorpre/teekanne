package controllers
import infrastructure.api.dto.ExpenseDto
import infrastructure.persistence.repositories.ExpenseRepository
import javax.inject._
import models.Expense
import play.api.libs.json.Json
import play.api.mvc._
import utils.DateConversionService

import scala.concurrent.{ExecutionContext, Future}


class ExpenseController @Inject()(repo: ExpenseRepository, cc: ControllerComponents)
                                 (implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def getExpense(id: Int)= Action.async { implicit request =>
    val maybeFutureExpense: Future[Option[Expense]] = repo.getExpenseById(id)
    for {
      maybeExpense <- maybeFutureExpense
    } yield maybeExpense match {
      case Some(expense) => Ok(Json.toJson(ExpenseDto(expense)))
      case _ => NotFound(Json.obj("error" -> "Expense not found"))
    }
  }

  def getExpensesBetween(from: String, to: String) = Action.async { implicit request =>
    val fromDate = DateConversionService.stringToDate(from)
    val toDate = DateConversionService.stringToDate(to)

    for {
      futureExpenses <- repo.getExpensesBetween(fromDate,toDate)
    } yield Ok(Json.toJson(futureExpenses.map(expense => ExpenseDto(expense))))
  }
}