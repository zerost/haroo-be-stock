package life.haroo.stock.entity.account

import life.haroo.stock.dto.account.AccountInfoResponseDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID

@Table(name = "securities_account_info")
class SecuritiesAccountInfo(
  @Id
  var securitiesAccountInfoId: UUID,
  var memberId: UUID,
  var securitiesCode: String,
  var encryptedAccountNumber: String,
  var accountTypeCode: String,
  var inquiryTimestamp: ZonedDateTime,
  var balance: BigDecimal,
  var createdAt: ZonedDateTime = ZonedDateTime.now(),
  var updatedAt: ZonedDateTime = ZonedDateTime.now(),
  var createdBy: String,
  var updatedBy: String,
  @MappedCollection(idColumn = "securities_account_info_id")
  var stockItems: MutableList<StockItemInfo> = mutableListOf(),
) {
  constructor() : this(
    securitiesAccountInfoId = UUID.randomUUID(),
    memberId = UUID.randomUUID(),
    securitiesCode = "",
    encryptedAccountNumber = "",
    accountTypeCode = "",
    inquiryTimestamp = ZonedDateTime.now(),
    balance = BigDecimal.ZERO,
    createdBy = "",
    updatedBy = "",
  )

  fun copyProperties(accountInfoResponseDto: AccountInfoResponseDto): SecuritiesAccountInfo {
    inquiryTimestamp = ZonedDateTime.now()
    balance = accountInfoResponseDto.output2
      .firstOrNull()?.totEvluAmt?.toBigDecimalOrNull() ?: throw RuntimeException() // TODO exception 처리

    accountInfoResponseDto.output1.forEach {
      stockItems.find { stockItem -> stockItem.stockCode == it.pdno }?.apply {
        stockQuantity = it.hldgQty.toBigDecimal()
        stockPrice = it.prpr.toBigDecimal()
        stockTotalPrice = it.evluAmt.toBigDecimal()
        referenceTimestamp = ZonedDateTime.now()
        updatedBy = ""
      } ?: run {
        stockItems.add(
          StockItemInfo(
            securitiesAccountInfoId = UUID.randomUUID(),
            stockCode = it.pdno,
            stockName = it.prdtName,
            stockQuantity = it.hldgQty.toBigDecimal(),
            stockPrice = it.prpr.toBigDecimal(),
            stockTotalPrice = it.evluAmt.toBigDecimal(),
            referenceTimestamp = ZonedDateTime.now(),
            createdBy = "",
            updatedBy = "",
          ),
        )
      }
    }
    return this
  }

  override fun hashCode(): Int {
    return securitiesAccountInfoId.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as SecuritiesAccountInfo

    if (securitiesAccountInfoId != other.securitiesAccountInfoId) return false
    if (memberId != other.memberId) return false
    if (securitiesCode != other.securitiesCode) return false
    if (encryptedAccountNumber != other.encryptedAccountNumber) return false
    if (accountTypeCode != other.accountTypeCode) return false
    if (inquiryTimestamp != other.inquiryTimestamp) return false
    if (balance != other.balance) return false
    if (updatedAt != other.updatedAt) return false

    return true
  }
}
