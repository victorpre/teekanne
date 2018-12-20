package infrastructure

import cats.effect.{ContextShift, IO}
import doobie.util.transactor.Transactor
import play.api.db.Database
import scala.concurrent.ExecutionContext

trait DbConfiguration {
  implicit val cs: ContextShift[IO]
  def transactor(db: Database, ec: ExecutionContext) =
    Transactor.fromConnection[IO](db.getConnection, ec)
}