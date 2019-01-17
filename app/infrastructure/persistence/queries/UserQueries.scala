package infrastructure.persistence.queries

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.{DbLoginInfo, DbUser, User}

import scala.concurrent.Future
import java.time.LocalTime
import java.util.UUID

import doobie.free.connection.ConnectionIO
import doobie.util.query.Query0
import doobie.implicits._
import infrastructure.persistence.DoobieCustomMapping

trait UserQueries extends DoobieCustomMapping {

  def selectUserById(id: UUID): Query0[DbUser] = {
    sql"""
      SELECT * from users
      WHERE id = $id
    """.query[DbUser]
  }

  def selectUserByLoginInfo(dbLoginInfo: DbLoginInfo): Query0[DbUser] = {
    sql"""
      SELECT u.* FROM users u
      JOIN user_login_info uli
        ON uli.user_id = u.id
      JOIN login_info li
        ON li.id = uli.login_info_id
      WHERE li.id = ${dbLoginInfo.id}
    """.query[DbUser]
  }

  def insertUser(dbUser: DbUser): ConnectionIO[DbUser] = {
    sql"""
      INSERT into users (first_name, last_name, email, avatar_url, activated)
      values (${dbUser.firstName}, ${dbUser.lastName}, ${dbUser.email},${dbUser.avatarUrl}, ${dbUser.activated})
    """.update
       .withUniqueGeneratedKeys("id", "first_name", "email", "avatar_url","activated")
  }
}
