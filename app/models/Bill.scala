package models
import java.sql.Date

case class Bill(id: Int, description: String, price: BigDecimal, purchaseDate: Date, location: String)