package controllers

import infrastructure.persistence.repositories.ExpenseRepository
import javax.inject._
import models.Expense
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Future

class HomeController @Inject()(cc: ControllerComponents, repo: ExpenseRepository)
  extends AbstractController(cc) {

  implicit private val ec = cc.executionContext

  def appSummary = Action.async { implicit request =>

    val maybeFutureExpense: Future[Option[Expense]] = repo.getExpenseById(1)

    for {
      maybeExpense: Option[Expense] <- maybeFutureExpense
    } yield maybeExpense match {
      case Some(expense) => Ok(Json.obj("content" -> s"Scala Play React Seed ${expense.description} with ${expense.price.toString()} at ${expense.purchaseDate.toString} ${expense.location}"))
      case _ => Ok(Json.obj("content" -> "not found"))
    }

//    futureBill.map { bill =>
////      repo.addBill(bill.text,bill.price,bill.purchaseDate,bill.location)
//      Ok(Json.obj("content" -> s"Scala Play React Seed ${bill.text} with ${bill.price.toString()} at ${bill.purchaseDate.toString}"))
//    }
  }
}

//    val newBill = repo.addBill("new",99.99,Date.valueOf("2018-08-08"),"target")
//
//    newBill.map( bill =>
//      Ok(Json.obj("content" -> s"Scala Play React Seed ${bill.text} with ${bill.price.toString()} at ${bill.purchaseDate.toString}"))
//    )
