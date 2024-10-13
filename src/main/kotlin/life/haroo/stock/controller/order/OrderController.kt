package life.haroo.stock.controller.order

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import life.haroo.stock.dto.common.CommonResponseDto
import life.haroo.stock.service.account.AccountQueryService
import life.haroo.stock.service.order.OrderService
import life.haroo.stock.utils.StockApiUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Tag(name = "Order Controller", description = "Controller for Order")
@RestController
@RequestMapping("/api/v1/stock/order")
class OrderController(
  private val orderService: OrderService,
  private val accountQueryService: AccountQueryService,
) {
  @Operation(summary = "Purchase Stock", description = "Purchases stock for a given member and securities account.")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "Stock purchased successfully"),
      ApiResponse(responseCode = "400", description = "Invalid input"),
      ApiResponse(responseCode = "500", description = "Internal server error"),
    ],
  )
  @PostMapping("/stock-orders")
  fun purchaseStock(): Mono<CommonResponseDto<Unit>> {
    return orderService.purchaseStock(StockApiUtils.MEMBER_ID, StockApiUtils.SECURITIES_ACCOUNT_INFO_ID)
  }
}
