package utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateConversionService {

  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

  def stringToDate(dateString: String): LocalDate =
    LocalDate.parse(dateString, formatter)

  def dateToString(date: LocalDate): String = date.format(formatter)
}

