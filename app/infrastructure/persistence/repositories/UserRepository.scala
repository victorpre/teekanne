package infrastructure.persistence.repositories

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import infrastructure.DbConfiguration
import infrastructure.persistence.queries.{DbLoginInfoQueries, UserLoginInfoQueries, UserQueries}
import javax.inject.Inject
import models.{DbLoginInfo, DbUser, DbUserLoginInfo, User}

import scala.concurrent.{ExecutionContext, Future}

trait UserRepository {
  def find(loginInfo: LoginInfo): Future[Option[User]]
  def find(userID: UUID): Future[Option[User]]
  def save(user: User): Future[User]
}


class UserRepositoryImpl  @Inject()(implicit ec: ExecutionContext)
  extends UserRepository with DbConfiguration with UserQueries with UserLoginInfoQueries
  with DbLoginInfoQueries {

  import doobie.implicits._
  import cats.effect.IO
  import com.mohiva.play.silhouette.api.LoginInfo

  override val cs = IO.contextShift(ec)

  override def save(user: User): Future[User] = {
    val dbUser = DbUser(user.userID.toString,user.firstName,user.lastName,user.email,user.avatarURL,user.activated)
    val dbLoginInfo = DbLoginInfo(None, user.loginInfo.providerID,user.loginInfo.providerKey)

    val maybeFutureLoginInfo = selectLoginInfoByProviderIdAndKey(dbLoginInfo.providerId, dbLoginInfo.providerKey).option.transact(xa).unsafeToFuture()

    val futureLoginInfo = maybeFutureLoginInfo.map(maybeLoginInfo => maybeLoginInfo match {
      case Some(loginInfo) => Future.successful(loginInfo)
      case None => insertLoginInfo(dbLoginInfo).transact(xa).unsafeToFuture()
    }).flatten


    for {
      _ <- insertUser(dbUser).transact(xa).unsafeToFuture()
      loginInfo <- futureLoginInfo
      dbUserLoginInfo <- insertUserLoginInfo(UUID.fromString(dbUser.userID),loginInfo.id.get).transact(xa).unsafeToFuture()
    } yield User(UUID.fromString(dbUser.userID), LoginInfo(loginInfo.providerId, loginInfo.providerKey), dbUser.firstName, dbUser.lastName, dbUser.lastName, dbUser.avatarUrl, dbUser.activated)
  }

  override def find(loginInfo: LoginInfo): Future[Option[User]] = {
    val queries = for {
      dbLoginInfo <- selectLoginInfoByProviderIdAndKey(loginInfo.providerID, loginInfo.providerKey).stream
      dbUserLoginInfo <- selectUserLoginInfoByLoginInfoId(dbLoginInfo.id.get).stream
      dbUser <- selectUserById(UUID.fromString(dbUserLoginInfo.userID)).stream
    } yield (dbUser, dbUserLoginInfo)

    val results = queries.compile.toList.transact(xa).unsafeToFuture().map(_.head)
    results.flatMap( resultSeq => buildUser(resultSeq._1,resultSeq._2))
  }

  override def find(userID: UUID): Future[Option[User]] = {
    val queries = for {
      dbUser <- selectUserById(userID).stream
      dbUserLoginInfo <- selectUserLoginInfoByUserId(userID).stream
    } yield (dbUser, dbUserLoginInfo)

    val results = queries.compile.toList.transact(xa).unsafeToFuture().map(_.head)
    results.flatMap( resultSeq => buildUser(resultSeq._1,resultSeq._2))
  }

  private def buildUser(dbUser: DbUser, dbUserLoginInfo: DbUserLoginInfo): Future[Option[User]] = {
    for {
      maybeLoginInfo  <- selectLoginInfoById(dbUserLoginInfo.loginInfoId).option.transact(xa).unsafeToFuture()
    } yield maybeLoginInfo match {
      case Some(loginInfo) =>
        Some(User(UUID.fromString(dbUser.userID),
          LoginInfo(loginInfo.providerId,loginInfo.providerKey),
          dbUser.firstName,
          dbUser.lastName,
          dbUser.email,
          dbUser.avatarUrl,
          dbUser.activated)
        )
      case _ => None
    }
  }

}