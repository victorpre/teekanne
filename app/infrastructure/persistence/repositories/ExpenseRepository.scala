package infrastructure.persistence.repositories

import java.time.LocalDate

import infrastructure.DbConfiguration
import infrastructure.persistence.queries.ExpenseQueries
import javax.inject.Inject
import models.Expense
import play.api.db.Database

import scala.concurrent.{ExecutionContext, Future}

trait ExpenseRepository {
  def getExpenseById(id: Int): Future[Option[Expense]]
  def getAllExpenses: Future[List[Expense]]
  def getExpensesSince(date: LocalDate): Future[List[Expense]]
  def getExpensesBetween(from: LocalDate, to: LocalDate): Future[List[Expense]]
  def addExpense(description: String, price: BigDecimal, purchaseDate: LocalDate, location: String): Future[Expense]

}

class ExpenseRepositoryImpl @Inject()(db: Database)
                                     (implicit ec: ExecutionContext)
  extends ExpenseRepository with DbConfiguration with ExpenseQueries   {
  import doobie.implicits._
  import cats.effect.IO

  override val cs = IO.contextShift(ec)

  val xa = transactor(db, ec)

  override def getExpenseById(id: Int): Future[Option[Expense]] = {
    selectExpenseById(id).option.transact(xa).unsafeToFuture()
  }

  override def getAllExpenses: Future[List[Expense]] = {
    selectAllExpenses.stream.compile.to[List].transact(xa).unsafeToFuture()
  }

  override def getExpensesSince(date: LocalDate): Future[List[Expense]] = {
    selectExpenseByPurchaseDate(date).stream.compile.toList.transact(xa).unsafeToFuture()
  }

  override def getExpensesBetween(from: LocalDate, to: LocalDate): Future[List[Expense]] = {
    selectExpensesBetweenPurchaseDates(from, to).stream.compile.toList.transact(xa).unsafeToFuture()
  }

  override def addExpense(text: String, price: BigDecimal, purchaseDate: LocalDate, location: String): Future[Expense] = {
    insertExpense(text, price, purchaseDate, location).transact(xa).unsafeToFuture()
  }
}