package life.haroo.stock.dto.account

data class Output(
  val ordPsblCash: String = "",
  val ordPsblSbst: String = "",
  val rusePsblAmt: String = "",
  val fundRpchChgs: String = "",
  val psblQtyCalcUnpr: String = "",
  val nrcvbBuyAmt: String = "",
  val nrcvbBuyQty: String = "",
  val maxBuyAmt: String = "",
  val maxBuyQty: String = "",
  val cmaEvluAmt: String = "",
  val ovrsReUseAmtWcrc: String = "",
  val ordPsblFrcrAmtWcrc: String = "",
)

/**
 *  매수가능조회 - /uapi/domestic-stock/v1/trading/inquire-psbl-order
 */
data class AccountResponseDto(
  val output: Output = Output(),
  val rtCd: String = "",
  val msgCd: String = "",
  val msg1: String = "",
)
