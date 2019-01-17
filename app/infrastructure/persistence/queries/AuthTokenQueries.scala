package infrastructure.persistence.queries

import java.time.LocalTime
import java.util.UUID

import doobie.free.connection.ConnectionIO
import doobie.util.query.Query0
import doobie.implicits._
import infrastructure.persistence.DoobieCustomMapping
import models.AuthToken
import org.joda.time.DateTime


trait AuthTokenQueries extends DoobieCustomMapping {

  def selectAuthTokenById(id: UUID): Query0[AuthToken] = {
    sql"""
      SELECT * from auth_tokens
      WHERE id = ${id}
    """.query[AuthToken]
  }

  def selectExpiredAuthTokens(expirationDate: DateTime): Query0[AuthToken] = {
    sql"""
      SELECT * from auth_tokens
      WHERE expiry < ${expirationDate}
    """.query[AuthToken]
  }

  def insertAuthToken(token: AuthToken): ConnectionIO[AuthToken] = {
    sql"""
    insert into auth_tokens (id, user_id, expiry) values (
      ${token.id}, ${token.userID}, ${token.expiry}
    """.update
    .withUniqueGeneratedKeys("id", "user_id", "expiry")
  }

  def deleteAuthToken(id: UUID): ConnectionIO[Int] =  {
    sql"""
      DELETE from auth_tokens
      WHERE id = $id
    """.update.run
  }
}
