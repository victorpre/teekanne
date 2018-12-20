package infrastructure.persistence.repositories

import infrastructure.persistence.queries.BillQueries
import infrastructure.persistence.tables.BillsTable
import javax.inject.Inject
import models.Bill
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.{JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

trait BillRepository {
  def findById(id: Int): Future[Bill]
}

class BillRepositoryImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                        (implicit ec: ExecutionContext)
  extends BillRepository with HasDatabaseConfigProvider[JdbcProfile]  with BillsTable with BillQueries   {

  import profile.api._

   override def findById(id: Int): Future[Bill] = {
    db.run(findByIdQuery(id).result).map(bills => bills.head)
  }

}