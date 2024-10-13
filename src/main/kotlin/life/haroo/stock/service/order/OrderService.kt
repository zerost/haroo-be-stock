package life.haroo.stock.service.order

import life.haroo.stock.dto.account.AccountResponseDto
import life.haroo.stock.dto.account.StockOrderResponse
import life.haroo.stock.dto.common.CommonResponseDto
import life.haroo.stock.entity.account.MemberSecuritiesInfo
import life.haroo.stock.entity.account.SecuritiesAccountInfo
import life.haroo.stock.repository.account.MemberSecuritiesInfoRepository
import life.haroo.stock.repository.account.SecuritiesAccountInfoRepository
import life.haroo.stock.repository.account.StockItemInfoRepository
import life.haroo.stock.service.account.AccountQueryService
import life.haroo.stock.service.member.MemberService
import life.haroo.stock.utils.StockApiUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import java.util.UUID

@Service
@Transactional(readOnly = true)
class OrderService(
  private val webClient: WebClient,
  private val securitiesAccountInfoRepository: SecuritiesAccountInfoRepository,
  private val memberService: MemberService,
) {
  fun purchaseStock(memberId: UUID, securitiesAccountInfoId: UUID): Mono<CommonResponseDto<Unit>> {
    val memberSecuritiesInfoMono = memberService.getMemberSecuritiesInfoMono(memberId)
    val securitiesAccountInfoMono = securitiesAccountInfoRepository.findById(securitiesAccountInfoId)

    return Mono.zip(memberSecuritiesInfoMono, securitiesAccountInfoMono)
      .flatMap { tuple: Tuple2<MemberSecuritiesInfo, SecuritiesAccountInfo> ->
        val memberSecuritiesInfo = tuple.t1
        val securitiesAccountInfo = tuple.t2
        val params = createCheckStockPurchaseAvailabilityParams(securitiesAccountInfo = securitiesAccountInfo)
        val headersMap = StockApiUtils.createHeadersMap(
          memberSecuritiesInfo = memberSecuritiesInfo,
          transactionId = "TTTC8908R",
        )

        checkStockPurchaseAvailability(params, headersMap)
          .flatMap { stockInfoResponse ->
            if (stockInfoResponse.rtCd == "0") {
              if (stockInfoResponse.output.nrcvbBuyQty.toInt() > 0) {
                Mono.just(stockInfoResponse)
              } else {
                Mono.error(RuntimeException("NOT_ERROR"))
              }
            } else {
              Mono.error(RuntimeException("Error: Received message is ${stockInfoResponse.msg1}"))
            }
          }
          .flatMap { stockInfoResponse ->
            val orderHeadersMap = StockApiUtils.createHeadersMap(
              memberSecuritiesInfo = memberSecuritiesInfo,
              transactionId = "TTTC0802U",
            )
            placeStockOrder(securitiesAccountInfo, orderHeadersMap)
          }
          .flatMap { stockOrderResponse ->
            if (stockOrderResponse.rtCd == "0") {
              Mono.just(CommonResponseDto<Unit>())
            } else {
              Mono.error(RuntimeException("Error: Received message is ${stockOrderResponse.msg1}"))
            }
          }
          .onErrorResume { e ->
            if (e.message == "NOT_ERROR") {
              Mono.just(CommonResponseDto())
            } else {
              throw e
            }
          }
      }
  }

  private fun createCheckStockPurchaseAvailabilityParams(securitiesAccountInfo: SecuritiesAccountInfo) =
    LinkedMultiValueMap<String, String>().apply {
      add("CANO", securitiesAccountInfo.encryptedAccountNumber.dropLast(2))
      add("ACNT_PRDT_CD", securitiesAccountInfo.encryptedAccountNumber.takeLast(2))
      add("PDNO", AccountQueryService.STOCK_CODE)
      add("ORD_UNPR", "")
      add("ORD_DVSN", "01")
      add("OVRS_ICLD_YN", "N")
      add("CMA_EVLU_AMT_ICLD_YN", "N")
    }

  private fun checkStockPurchaseAvailability(
    params: LinkedMultiValueMap<String, String>,
    headersMap: Map<String, String>,
  ) = webClient.get()
    .uri(AccountQueryService.BASE_URL + "/uapi/domestic-stock/v1/trading/inquire-psbl-order") { builder ->
      builder.queryParams(params).build()
    }.headers { headers ->
      headersMap.forEach { (key, value) ->
        headers.set(key, value)
      }
    }
    .retrieve()
    .bodyToMono(AccountResponseDto::class.java)
    .log()


  private fun placeStockOrder(
    securitiesAccountInfo: SecuritiesAccountInfo,
    headersMap: Map<String, String>,
  ) = webClient.post()
    .uri(AccountQueryService.BASE_URL + "/uapi/domestic-stock/v1/trading/order-cash")
    .headers { headers ->
      headersMap.forEach { (key, value) ->
        headers.set(key, value)
      }
    }
    .bodyValue(
      mapOf(
        "CANO" to securitiesAccountInfo.encryptedAccountNumber.dropLast(2),
        "ACNT_PRDT_CD" to securitiesAccountInfo.encryptedAccountNumber.takeLast(2),
        "PDNO" to AccountQueryService.STOCK_CODE,
        "ORD_DVSN" to "01", // 주문구분 - 01:시장가
        "ORD_QTY" to "1", // 주문수량
        "ORD_UNPR" to "0",
      ),
    )
    .retrieve()
    .bodyToMono(StockOrderResponse::class.java)
    .log()
}
