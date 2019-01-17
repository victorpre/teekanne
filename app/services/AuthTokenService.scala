package services

import scala.language.postfixOps
import java.util.UUID

import javax.inject.Inject
import com.mohiva.play.silhouette.api.util.Clock
import infrastructure.persistence.repositories.AuthTokenRepository
import models.AuthToken
import org.joda.time.DateTimeZone

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps


trait AuthTokenService {

  def create(userID: UUID, expiry: FiniteDuration = 5 minutes): Future[AuthToken]
  def validate(id: UUID): Future[Option[AuthToken]]
  def clean: Future[Seq[Int]]
}

class AuthTokenServiceImpl @Inject()(authTokenRepo: AuthTokenRepository, clock: Clock)
(implicit ex: ExecutionContext) extends AuthTokenService {
  override def create(userID: UUID, expiry: FiniteDuration): Future[AuthToken] = {
    val token = AuthToken(UUID.randomUUID(), userID, clock.now.withZone(DateTimeZone.UTC).plusSeconds(expiry.toSeconds.toInt))
    authTokenRepo.save(token)
  }

  override def validate(id: UUID): Future[Option[AuthToken]] = authTokenRepo.find(id)

  override def clean: Future[Seq[Int]] = {
     val expiredTokens: Future[Seq[AuthToken]] = authTokenRepo.findExpired(clock.now.withZone(DateTimeZone.UTC))
     expiredTokens.flatMap( tokens => Future.sequence(tokens.map( token => authTokenRepo.remove(token.id))))
  }
}
