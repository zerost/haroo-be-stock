package life.haroo.stock.dto.account

import com.fasterxml.jackson.annotation.JsonFormat
import life.haroo.stock.utils.DateUtils
import java.time.LocalDateTime

data class Token2ResponseDto(
  var accessToken: String = "",
  @set:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  var accessTokenTokenExpired: LocalDateTime = DateUtils.MIN_TIMESTAMP,
  var tokenType: String = "",
  var expiresIn: Long = 0,
)
