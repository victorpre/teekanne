package models
import java.sql.Date

case class Bill(id: Int, text: String, price: BigDecimal, purchaseDate: Date)