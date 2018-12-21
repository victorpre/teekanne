package infrastructure.api.dto

import java.sql.Date
import models.Bill
import play.api.libs.json.Json

case class BillDto(description: String, price: BigDecimal, purchaseDate: String, location: String)

object BillDto {
  implicit val jsonWrites = Json.writes[BillDto]

  def apply(bill: Bill): BillDto = {
    BillDto(
      bill.description,
      bill.price,
      dateToString(bill.purchaseDate),
      bill.location
    )
  }

  private def dateToString(date: Date, format:String = "dd/MM/YYYY"): String = {
    import java.text._
    val sdf = new SimpleDateFormat(format)
    sdf.format(date)
  }
}