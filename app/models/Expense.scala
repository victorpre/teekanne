package models
import java.time.LocalDate

case class Expense(id: Int, description: String, price: BigDecimal, purchaseDate: LocalDate, location: String)