package life.haroo.stock.entity.account

import life.haroo.stock.dto.account.Output1
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID

@Table(name = "stock_item_info")
class StockItemInfo(
  @Id
  var stockItemInfoId: UUID = UUID.randomUUID(),
  var securitiesAccountInfoId: UUID,
  var stockCode: String,
  var stockName: String,
  var stockQuantity: BigDecimal,
  var stockPrice: BigDecimal,
  var stockTotalPrice: BigDecimal,
  var referenceTimestamp: ZonedDateTime,
  var createdAt: ZonedDateTime = ZonedDateTime.now(),
  var updatedAt: ZonedDateTime = ZonedDateTime.now(),
  var createdBy: String,
  var updatedBy: String,
) {

  constructor(securitiesAccountInfoId: UUID, output: Output1) : this(
    securitiesAccountInfoId = securitiesAccountInfoId,
    stockCode = output.pdno,
    stockName = output.prdtName,
    stockQuantity = output.hldgQty.toBigDecimal(),
    stockPrice = output.prpr.toBigDecimal(),
    stockTotalPrice = output.evluAmt.toBigDecimal(),
    referenceTimestamp = ZonedDateTime.now(),
    createdBy = "system",
    updatedBy = "system",
  )

  fun copyProperties(output: Output1): StockItemInfo {
    stockQuantity = output.hldgQty.toBigDecimal()
    stockPrice = output.prpr.toBigDecimal()
    stockPrice = output.evluAmt.toBigDecimal()
    return this
  }
}
