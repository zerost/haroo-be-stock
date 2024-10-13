package life.haroo.stock.dto.account

import io.swagger.v3.oas.annotations.media.Schema
import life.haroo.stock.entity.account.SecuritiesAccountInfo
import life.haroo.stock.entity.account.StockItemInfo
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID

@Schema(description = "Account Information Data Transfer Object")
data class AccountInfoDto(
  @Schema(
    description = "Unique identifier for the securities account info",
    example = "123e4567-e89b-12d3-a456-426614174000",
  )
  val securitiesAccountInfoId: UUID,
  @Schema(description = "Unique identifier for the member", example = "123e4567-e89b-12d3-a456-426614174000")
  val memberId: UUID,
  @Schema(description = "Securities code", example = "A000660")
  val securitiesCode: String,
  @Schema(description = "Encrypted account number", example = "1234567890")
  val encryptedAccountNumber: String,
  @Schema(description = "Account type code", example = "01")
  val accountTypeCode: String,
  @Schema(description = "Inquiry timestamp", example = "2021-08-01T00:00:00Z")
  val inquiryTimestamp: ZonedDateTime,
  @Schema(description = "Account balance", example = "1000000")
  val balance: String,
  @Schema(description = "List of stock items")
  val stockItems: List<StockItemInfoDto>,
) {
  constructor(securitiesAccountInfo: SecuritiesAccountInfo, stockItemInfoList: MutableList<StockItemInfo>) : this(
    securitiesAccountInfoId = securitiesAccountInfo.securitiesAccountInfoId,
    memberId = securitiesAccountInfo.memberId,
    securitiesCode = securitiesAccountInfo.securitiesCode,
    encryptedAccountNumber = securitiesAccountInfo.encryptedAccountNumber,
    accountTypeCode = securitiesAccountInfo.accountTypeCode,
    inquiryTimestamp = securitiesAccountInfo.inquiryTimestamp,
    balance = securitiesAccountInfo.balance.toString(),
    stockItems = stockItemInfoList.map { StockItemInfoDto(it) },
  )
}

@Schema(description = "Stock Item Information Data Transfer Object")
data class StockItemInfoDto(
  @Schema(
    description = "Unique identifier for the stock item info",
    example = "123e4567-e89b-12d3-a456-426614174000",
  )
  val stockItemInfoId: UUID,
  @Schema(description = "Stock code", example = "A000660")
  val stockCode: String,
  @Schema(description = "Stock name", example = "SK하이닉스")
  val stockName: String,
  @Schema(description = "Stock quantity", example = "100")
  val stockQuantity: BigDecimal,
  @Schema(description = "Stock price", example = "100000")
  val stockPrice: BigDecimal,
  @Schema(description = "Stock total price", example = "10000000")
  val stockTotalPrice: BigDecimal,
  @Schema(title = "Reference timestamp", description = "Reference timestamp", example = "2021-08-01T00:00:00Z")
  val referenceTimestamp: ZonedDateTime,
) {
  constructor(stockItemInfoId: StockItemInfo) : this(
    stockItemInfoId = stockItemInfoId.stockItemInfoId,
    stockCode = stockItemInfoId.stockCode,
    stockName = stockItemInfoId.stockName,
    stockQuantity = stockItemInfoId.stockQuantity,
    stockPrice = stockItemInfoId.stockPrice,
    stockTotalPrice = stockItemInfoId.stockTotalPrice,
    referenceTimestamp = stockItemInfoId.referenceTimestamp,
  )
}
