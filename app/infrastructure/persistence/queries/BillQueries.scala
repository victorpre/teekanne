package infrastructure.persistence.queries

import java.time.LocalDate

import doobie.free.connection.ConnectionIO
import doobie.util.query.Query0
import doobie.implicits._
import models.Bill

trait BillQueries {

  def selectAllBills: Query0[Bill] = {
    sql"""
      SELECT * from bills
    """.query
  }

  def selectBillById(id: Int): Query0[Bill] = {
    sql"""
      SELECT * from bills
      WHERE id = $id
    """.query[Bill]
  }

  def selectBillByPurchaseDate(purchaseDate: LocalDate): Query0[Bill] = {
    sql"""
      SELECT * from bills
      WHERE purchase_date >= $purchaseDate
    """.query[Bill]
  }

  def selectBillBetweenPurchaseDates(from: LocalDate, to: LocalDate): Query0[Bill] = {
    sql"""
      SELECT * from bills
      WHERE purchase_date >= $from
       AND
       purchase_date <= $to
    """.query[Bill]
  }

  def insertBill(description: String, price: BigDecimal, purchaseDate: LocalDate, location: String): ConnectionIO[Bill] = {
    sql"insert into bills (description, price, purchase_date, location) values ($description, $price, $purchaseDate, $location)"
      .update
      .withUniqueGeneratedKeys("id", "description", "price", "purchase_date","location")
  }
}