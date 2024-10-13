package life.haroo.stock.exception

import life.haroo.stock.dto.common.ErrorResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException::class)
  fun handleRuntimeException(e: RuntimeException): ResponseEntity<ErrorResponseDto> {
    val httpStatus = HttpStatus.BAD_REQUEST
    val errorResponse = ErrorResponseDto(
      status = httpStatus.value(),
      message = e.message ?: "An error occurred",
    )
    return ResponseEntity(errorResponse, httpStatus)
  }
}
