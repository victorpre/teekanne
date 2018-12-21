package infrastructure.persistence.repositories

import java.time.LocalDate

import infrastructure.DbConfiguration
import infrastructure.persistence.queries.BillQueries
import javax.inject.Inject
import models.Bill
import play.api.db.Database

import scala.concurrent.{ExecutionContext, Future}

trait BillRepository {
  def getBillById(id: Int): Future[Option[Bill]]
  def getAllBills: Future[List[Bill]]
  def getBillsFrom(date: LocalDate): Future[List[Bill]]
  def getBillsBetween(from: LocalDate, to: LocalDate): Future[List[Bill]]
  def addBill(description: String, price: BigDecimal, purchaseDate: LocalDate, location: String): Future[Bill]

}

class BillRepositoryImpl @Inject()(db: Database)
                        (implicit ec: ExecutionContext)
  extends BillRepository with DbConfiguration with BillQueries   {
  import doobie.implicits._
  import cats.effect.IO

  override val cs = IO.contextShift(ec)

  val xa = transactor(db, ec)

  override def getBillById(id: Int): Future[Option[Bill]] = {
    selectBillById(id).option.transact(xa).unsafeToFuture()
  }

  override def getAllBills: Future[List[Bill]] = {
    selectAllBills.stream.compile.to[List].transact(xa).unsafeToFuture()
  }

  override def getBillsFrom(date: LocalDate): Future[List[Bill]] = {
    selectBillByPurchaseDate(date).stream.compile.toList.transact(xa).unsafeToFuture()
  }

  override def getBillsBetween(from: LocalDate,to: LocalDate): Future[List[Bill]] = {
    selectBillBetweenPurchaseDates(from, to).stream.compile.toList.transact(xa).unsafeToFuture()
  }

  override def addBill(text: String, price: BigDecimal, purchaseDate: LocalDate, location: String): Future[Bill] = {
    insertBill(text, price, purchaseDate, location).transact(xa).unsafeToFuture()
  }
}