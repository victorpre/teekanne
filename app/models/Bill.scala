package models
import java.time.LocalDate

case class Bill(id: Int, description: String, price: BigDecimal, purchaseDate: LocalDate, location: String)