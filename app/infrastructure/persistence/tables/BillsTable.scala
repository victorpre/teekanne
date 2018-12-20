package infrastructure.persistence.tables

import models.Bill
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import java.sql.Date

trait BillsTable {
   self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class BillsTable(tag: Tag) extends Table[Bill](tag, "bills") {
    def id = column[Int]("id")
    def text = column[String]("text")
    def price = column[BigDecimal]("price")
    def purchaseDate = column[Date]("purchase_date")



    def * = (id, text, price, purchaseDate) <> (Bill.tupled, Bill.unapply)
  }
  val billsTable = TableQuery[BillsTable]
}
