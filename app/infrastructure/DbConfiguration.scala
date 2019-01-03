package infrastructure

import cats.effect.{ContextShift, IO}
import com.typesafe.config.ConfigFactory
import doobie.util.transactor.Transactor

trait DbConfiguration {
  implicit val cs: ContextShift[IO]

  val config = ConfigFactory.load()

  def xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    config.getString("db.default.url"),
    config.getString("db.default.username"),
    config.getString("db.default.password")
  )
}
