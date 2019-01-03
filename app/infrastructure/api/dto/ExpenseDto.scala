package infrastructure.api.dto


import models.Expense
import play.api.libs.json.Json
import utils.DateConversionService

case class ExpenseDto(description: String, price: BigDecimal, purchase_date: String, location: String)

object ExpenseDto {
  implicit val jsonWrites = Json.writes[ExpenseDto]

  def apply(expense: Expense): ExpenseDto = {
    ExpenseDto(
      expense.description,
      expense.price,
      DateConversionService.dateToString(expense.purchaseDate),
      expense.location
    )
  }
}