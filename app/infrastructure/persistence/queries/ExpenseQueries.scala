package infrastructure.persistence.queries

import java.time.LocalDate

import doobie.free.connection.ConnectionIO
import doobie.util.query.Query0
import doobie.implicits._
import models.Expense

trait ExpenseQueries {

  def selectAllExpenses: Query0[Expense] = {
    sql"""
      SELECT * from expenses
    """.query
  }

  def selectExpenseById(id: Int): Query0[Expense] = {
    sql"""
      SELECT * from expenses
      WHERE id = $id
    """.query[Expense]
  }

  def selectExpenseByPurchaseDate(purchaseDate: LocalDate): Query0[Expense] = {
    sql"""
      SELECT * from expenses
      WHERE purchase_date >= $purchaseDate
    """.query[Expense]
  }

  def selectExpensesBetweenPurchaseDates(from: LocalDate, to: LocalDate): Query0[Expense] = {
    sql"""
      SELECT * from expenses
      WHERE purchase_date >= $from
       AND
       purchase_date <= $to
    """.query[Expense]
  }

  def insertExpense(description: String, price: BigDecimal, purchaseDate: LocalDate, location: String): ConnectionIO[Expense] = {
    sql"insert into expenses (description, price, purchase_date, location) values ($description, $price, $purchaseDate, $location)"
      .update
      .withUniqueGeneratedKeys("id", "description", "price", "purchase_date","location")
  }
}