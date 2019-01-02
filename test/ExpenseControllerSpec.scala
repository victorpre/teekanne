
import java.time.LocalDate

import controllers.ExpenseController
import infrastructure.api.dto.ExpenseDto
import infrastructure.persistence.repositories.ExpenseRepository
import models.Expense
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import org.scalamock.scalatest.MockFactory
import play.api.libs.json.Json


import scala.concurrent.Future


class ExpenseControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockFactory {
  "ExpenseController#getExpense" should {
    "respond with OK to a valid request" in {
      val repo = stub[ExpenseRepository]
      val controller = new ExpenseController(repo, stubControllerComponents())
      val expense = Expense(1,"Desc",10.0,LocalDate.now(),"Rewe")
      (repo.getExpenseById(_: Int)).when(*).returns(Future.successful(Some(expense)))

      val home = controller.getExpense(1).apply(FakeRequest(GET, "/api/expenses/1"))

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      val resultJson = contentAsJson(home)
      resultJson mustBe Json.toJson(ExpenseDto(expense))
    }

    "respond with NotFound when id provided is unavailable" in {
      val repo = stub[ExpenseRepository]
      val controller = new ExpenseController(repo, stubControllerComponents())
      (repo.getExpenseById(_: Int)).when(*).returns(Future.successful(None))

      val home = controller.getExpense(-1).apply(FakeRequest(GET, "/api/expenses/-1"))

      status(home) mustEqual NOT_FOUND
      contentType(home) mustBe Some("application/json")
      val resultJson = contentAsJson(home)
      resultJson mustBe Json.obj("error" -> "Expense not found")
    }
  }

  "ExpenseController#getExpensesBetween" should {
    "respond with OK to a valid request " in {
      val repo = stub[ExpenseRepository]
      val controller = new ExpenseController(repo, stubControllerComponents())
      val expenses = List(Expense(1,"Desc",10.0,LocalDate.parse("2000-01-01"),"Rewe"),
        Expense(2,"Desc",10.0,LocalDate.parse("2000-01-01"),"Rewe"))
      (repo.getExpensesBetween(_: LocalDate, _: LocalDate)).when(*,*).returns(Future.successful(expenses))

      val home = controller.getExpensesBetween("01/01/2000","01/01/2000").apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      val resultJson = contentAsJson(home)
      resultJson mustBe Json.obj("collection" -> expenses.map(e => ExpenseDto(e)))
    }
  }
}
