package infrastructure.persistence.repositories

import infrastructure.DbConfiguration
import infrastructure.persistence.queries.BillQueries
import javax.inject.Inject
import models.Bill
import play.api.db.Database

import scala.concurrent.{ExecutionContext, Future}

trait BillRepository {
  def findById(id: Int): Future[Bill]
  def getAll: Future[List[Bill]]

}

class BillRepositoryImpl @Inject()(db: Database)
                        (implicit ec: ExecutionContext)
  extends BillRepository with DbConfiguration with BillQueries   {
  import doobie.implicits._
  import cats.effect.IO

  override val cs = IO.contextShift(ec)
  val xa = transactor(db, ec)

  override def findById(id: Int): Future[Bill] = {
    selectById(id).stream.compile.toList.transact(xa).unsafeToFuture().map(_.head)
  }

  override def getAll: Future[List[Bill]] = {
    selectAll.stream.compile.to[List].transact(xa).unsafeToFuture()
  }
}