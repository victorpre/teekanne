package infrastructure.persistence.queries

import java.util.UUID

import doobie.free.connection.ConnectionIO
import doobie.util.query.Query0
import doobie.implicits._
import infrastructure.persistence.DoobieCustomMapping
import models.DbUserLoginInfo

trait UserLoginInfoQueries extends DoobieCustomMapping {
  def selectUserLoginInfoByUserId(userId: UUID): Query0[DbUserLoginInfo] = {
    sql"""
      SELECT * from user_login_info
      WHERE user_id = ${userId.toString}
    """.query[DbUserLoginInfo]
  }

  def selectUserLoginInfoByLoginInfoId(loginInfoId: Int): Query0[DbUserLoginInfo] = {
    sql"""
      SELECT * from user_login_info
      WHERE login_info_id = $loginInfoId
    """.query[DbUserLoginInfo]
  }

  def insertUserLoginInfo(userId: UUID, loginInfoId: Int): ConnectionIO[DbUserLoginInfo] = {
    sql"""
      INSERT INTO user_login_info (user_id, login_info_id)
      values (${userId}, ${loginInfoId})
    """.update
      .withUniqueGeneratedKeys("id","user_id", "login_info_id")
  }
}
