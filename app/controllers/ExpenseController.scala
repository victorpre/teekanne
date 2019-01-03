package controllers
import infrastructure.api.dto.ExpenseDto
import infrastructure.persistence.repositories.ExpenseRepository
import javax.inject._
import models.Expense
import play.api.libs.json.Json
import play.api.mvc._
import utils.DateConversionService

import scala.concurrent.Future


class ExpenseController @Inject()(cc: ControllerComponents, repo: ExpenseRepository)
  extends AbstractController(cc) {

  implicit private val ec = cc.executionContext

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
    } yield Ok(Json.obj("collection" -> Json.toJson(futureExpenses.map(expense => ExpenseDto(expense)))))
  }
}
