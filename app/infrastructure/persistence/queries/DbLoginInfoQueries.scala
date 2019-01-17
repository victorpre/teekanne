package infrastructure.persistence.queries

import doobie.free.connection.ConnectionIO
import doobie.util.query.Query0
import doobie.implicits._
import models.DbLoginInfo

trait DbLoginInfoQueries {
  def selectLoginInfoById(id: Int): Query0[DbLoginInfo] = {
    sql"""
      SELECT * from login_info
      WHERE id = $id
    """.query[DbLoginInfo]
  }

  def selectLoginInfoByProviderIdAndKey(providerId: String, providerKey: String): Query0[DbLoginInfo] = {
    sql"""
      SELECT * from login_info
      WHERE provider_id = $providerId
      AND provider_key = $providerKey
    """.query[DbLoginInfo]
  }

  def insertLoginInfo(loginInfo: DbLoginInfo): ConnectionIO[DbLoginInfo] = {
    sql"""
      INSERT INTO login_info (provider_id, provider_key)
      values (${loginInfo.providerId}, ${loginInfo.providerKey})
    """.update
      .withUniqueGeneratedKeys("id","provider_id", "provider_key")
  }
}
