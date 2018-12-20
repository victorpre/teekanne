package infrastructure.persistence.queries

import infrastructure.persistence.tables.BillsTable
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait BillQueries {
  self: HasDatabaseConfigProvider[JdbcProfile] with BillsTable =>

  import profile.api._

  val findByIdQuery = Compiled { (billId: Rep[Int]) =>
    billsTable.filter(b => b.id === billId)
  }
}
