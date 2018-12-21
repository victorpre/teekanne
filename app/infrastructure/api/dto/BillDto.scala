package infrastructure.api.dto


import models.Bill
import play.api.libs.json.Json
import utils.DateConversionService

case class BillDto(description: String, price: BigDecimal, purchase_date: String, location: String)

object BillDto {
  implicit val jsonWrites = Json.writes[BillDto]

  def apply(bill: Bill): BillDto = {
    BillDto(
      bill.description,
      bill.price,
      DateConversionService.dateToString(bill.purchaseDate),
      bill.location
    )
  }
}