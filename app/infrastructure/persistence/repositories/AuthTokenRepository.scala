package infrastructure.persistence.repositories

import java.time.LocalTime
import java.util.UUID

import infrastructure.DbConfiguration
import infrastructure.persistence.queries.AuthTokenQueries
import models.AuthToken
import javax.inject.Inject
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future}

trait AuthTokenRepository {
  def find(id: UUID): Future[Option[AuthToken]]
  def findExpired(expirationDate: DateTime): Future[Seq[AuthToken]]
  def save(token: AuthToken): Future[AuthToken]
  def remove(id: UUID): Future[Int]
}


class AuthTokenRepositoryImpl @Inject()(implicit ec: ExecutionContext)
  extends AuthTokenRepository with DbConfiguration with AuthTokenQueries {

  import doobie.implicits._
  import cats.effect.IO

  override val cs = IO.contextShift(ec)

  override def find(id: UUID): Future[Option[AuthToken]] = {
    selectAuthTokenById(id).option.transact(xa).unsafeToFuture()
  }

  override def findExpired(expirationDate: DateTime): Future[Seq[AuthToken]] = {
    selectExpiredAuthTokens(expirationDate).stream.compile.to[Seq].transact(xa).unsafeToFuture()
  }

  override def save(token: AuthToken): Future[AuthToken] = {
    insertAuthToken(token).transact(xa).unsafeToFuture()
  }

  override def remove(id: UUID): Future[Int] = {
    deleteAuthToken(id).transact(xa).unsafeToFuture()
  }
}