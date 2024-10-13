package life.haroo.stock.dto.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL

@JsonInclude(NON_NULL)
data class CommonResponseDto<T>(
  val status: String = "ok",
  val message: String = "success",
  val data: T? = null,
)
