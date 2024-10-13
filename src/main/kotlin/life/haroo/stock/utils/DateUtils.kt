package life.haroo.stock.utils

import java.time.LocalDate
import java.time.LocalDateTime

class DateUtils {
  companion object {
    val MIN_DATE = LocalDate.of(1, 1, 1)
    val MIN_TIMESTAMP = LocalDateTime.of(1, 1, 1, 0, 0, 0)
  }
}
