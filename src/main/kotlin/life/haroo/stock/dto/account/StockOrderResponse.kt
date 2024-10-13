package life.haroo.stock.dto.account

data class StockOrderResponse(
  val rtCd: String = "",
  val msgCd: String = "",
  val msg1: String = "",
  val output: StockOrderOutput = StockOrderOutput(),
)

data class StockOrderOutput(
  val krxFwdgOrdOrgno: String = "",
  val odno: String = "",
  val ordTmd: String = "",
)
