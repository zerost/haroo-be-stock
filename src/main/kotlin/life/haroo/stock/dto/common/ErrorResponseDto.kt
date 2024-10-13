package life.haroo.stock.dto.common

import java.time.ZonedDateTime

data class ErrorResponseDto(
  val status: Int,
  val message: String,
  val timestamp: ZonedDateTime = ZonedDateTime.now(),
)
