package life.haroo.stock.dto.account

data class AccountInfoResponseDto(
  val ctxAreaFk100: String = "",
  val ctxAreaNk100: String = "",
  val output1: List<Output1> = emptyList(),
  val output2: List<Output2> = emptyList(),
  val rtCd: String = "",
  val msgCd: String = "",
  val msg1: String = "",
)

data class Output1(
  val pdno: String = "",
  val prdtName: String = "",
  val tradDvsnName: String = "",
  val bfdyBuyQty: String = "",
  val bfdySllQty: String = "",
  val thdtBuyqty: String = "",
  val thdtSllQty: String = "",
  val hldgQty: String = "",
  val ordPsblQty: String = "",
  val pchsAvgPric: String = "",
  val pchsAmt: String = "",
  val prpr: String = "",
  val evluAmt: String = "",
  val evluPflsAmt: String = "",
  val evluPflsRt: String = "",
  val evluErngRt: String = "",
  val loanDt: String = "",
  val loanAmt: String = "",
  val stlnSlngChgs: String = "",
  val expdDt: String = "",
  val flttRt: String = "",
  val bfdyCprsIcdc: String = "",
  val itemMgnaRtName: String = "",
  val grtaRtName: String = "",
  val sbstPric: String = "",
  val stckLoanUnpr: String = "",
)

data class Output2(
  val dncaTotAmt: String = "",
  val nxdyExccAmt: String = "",
  val prvsRcdlExccAmt: String = "",
  val cmaEvluAmt: String = "",
  val bfdyBuyAmt: String = "",
  val thdtBuyAmt: String = "",
  val nxdyAutoRdptAmt: String = "",
  val bfdySllAmt: String = "",
  val thdtSllAmt: String = "",
  val d2AutoRdptAmt: String = "",
  val bfdyTlexAmt: String = "",
  val thdtTlexAmt: String = "",
  val totLoanAmt: String = "",
  val sctsEvluAmt: String = "",
  val totEvluAmt: String = "",
  val nassAmt: String = "",
  val fncgGldAutoRdptYn: String = "",
  val pchsAmtSmtlAmt: String = "",
  val evluAmtSmtlAmt: String = "",
  val evluPflsSmtlAmt: String = "",
  val totStlnSlngChgs: String = "",
  val bfdyTotAsstEvluAmt: String = "",
  val asstIcdcAmt: String = "",
  val asstIcdcErngRt: String = "",
)
