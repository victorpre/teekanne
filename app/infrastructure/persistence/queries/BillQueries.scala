package infrastructure.persistence.queries

import doobie.util.query.Query0

import doobie.implicits._

import models.Bill

trait BillQueries {

  def selectAll: Query0[Bill] = {
    sql"""
      SELECT * from bills
      """.query
  }

  def selectById(id: Int): Query0[Bill] = {
    sql"""
      SELECT * from bills
      WHERE id = $id
      """.query[Bill]
  }
}
