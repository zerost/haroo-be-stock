package life.haroo.stock.controller.account

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import life.haroo.stock.dto.account.AccountInfoDto
import life.haroo.stock.dto.common.CommonResponseDto
import life.haroo.stock.service.account.AccountQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@Tag(name = "Account Query Controller", description = "Controller for account queries")
@RestController
@RequestMapping("/api/v1/stock/account")
class AccountQueryController(private val accountQueryService: AccountQueryService) {
  val memberId = UUID.fromString("0ad9acba-d99d-4f74-8784-1a4a62e21689") // TODO 파라미터로 받아야함
  val securitiesAccountInfoId = UUID.fromString("ef9cad63-d059-4033-b16f-5f586257bd82") // TODO 파라미터로 받아야함

  @Operation(
    summary = "Store Account Info",
    description = "Stores account information for a given member and securities account.",
  )
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "Account information stored successfully"),
      ApiResponse(responseCode = "400", description = "Invalid input"),
      ApiResponse(responseCode = "500", description = "Internal server error"),
    ],
  )
  @PostMapping("/stock-account")
  fun storeAccountInfo(): Mono<CommonResponseDto<Unit>> {
    return accountQueryService.storeAccountInfo(memberId = memberId, securitiesAccountInfoId = securitiesAccountInfoId)
  }

  @Operation(summary = "Store Token", description = "Stores a securities token for a given member.")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "Token stored successfully"),
      ApiResponse(responseCode = "400", description = "Invalid input"),
      ApiResponse(responseCode = "500", description = "Internal server error"),
    ],
  )
  @PostMapping("/token")
  fun storeToken(): Mono<CommonResponseDto<Unit>> {
    return accountQueryService.storeSecuritiesToken(memberId)
  }



  @Operation(
    summary = "Retrieve Account Info",
    description = "Retrieves account information for a given member and securities account.",
  )
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "Account information retrieved successfully",
        content = [Content(schema = Schema(implementation = AccountInfoDto::class))],
      ),
      ApiResponse(responseCode = "400", description = "Invalid input"),
      ApiResponse(responseCode = "500", description = "Internal server error"),
    ],
  )
  @GetMapping("/stock-account")
  fun retrieveAccountInfo(): Mono<CommonResponseDto<AccountInfoDto>> {
    return accountQueryService.retrieveAccountInfo(memberId, securitiesAccountInfoId)
  }
}
