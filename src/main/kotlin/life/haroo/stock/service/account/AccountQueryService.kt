package life.haroo.stock.service.account

import life.haroo.stock.dto.account.AccountInfoDto
import life.haroo.stock.dto.account.AccountInfoResponseDto
import life.haroo.stock.dto.account.Output1
import life.haroo.stock.dto.account.Token2ResponseDto
import life.haroo.stock.dto.common.CommonResponseDto
import life.haroo.stock.entity.account.MemberSecuritiesInfo
import life.haroo.stock.entity.account.SecuritiesAccountInfo
import life.haroo.stock.entity.account.StockItemInfo
import life.haroo.stock.repository.account.MemberSecuritiesInfoRepository
import life.haroo.stock.repository.account.SecuritiesAccountInfoRepository
import life.haroo.stock.repository.account.StockItemInfoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import reactor.util.function.Tuple3
import java.util.UUID

@Service
@Transactional(readOnly = true)
class AccountQueryService(
  private val webClient: WebClient,
  private val memberSecuritiesInfoRepository: MemberSecuritiesInfoRepository,
  private val securitiesAccountInfoRepository: SecuritiesAccountInfoRepository,
  private val stockItemInfoRepository: StockItemInfoRepository,
) {
  companion object {
    val BASE_URL = "https://openapi.koreainvestment.com:9443"
    val STOCK_CODE = "458730"
  }

  @Transactional(readOnly = false)
  fun storeSecuritiesToken(memberId: UUID): Mono<CommonResponseDto<Unit>> {
    return getMemberSecuritiesInfoMono(memberId)
      .flatMap { memberInfo ->
        webClient.post()
          .uri(BASE_URL + "/oauth2/tokenP")
          .bodyValue(
            mapOf(
              "grant_type" to "client_credentials",
              "appkey" to memberInfo.appKey,
              "appsecret" to memberInfo.appSecretValue,
            ),
          )
          .retrieve()
          .bodyToMono(Token2ResponseDto::class.java)
          .log()
          .map { tokenResponse -> memberInfo to tokenResponse }
      }
      .flatMap { (memberInfo, tokenResponse) ->
        memberSecuritiesInfoRepository.save(memberInfo.copyProperties(tokenResponse))
      }
      .then(Mono.just(CommonResponseDto()))
  }

  @Transactional(readOnly = false)
  fun storeAccountInfo(memberId: UUID, securitiesAccountInfoId: UUID): Mono<CommonResponseDto<Unit>> {
    val memberSecuritiesInfoMono = getMemberSecuritiesInfoMono(memberId = memberId)
    val securitiesAccountInfoMono = securitiesAccountInfoRepository.findById(securitiesAccountInfoId)
    val stockItemInfoListMono = stockItemInfoRepository
      .findAllBySecuritiesAccountInfoId(securitiesAccountInfoId).collectList()

    return Mono.zip(memberSecuritiesInfoMono, securitiesAccountInfoMono, stockItemInfoListMono)
      .flatMap { tuple: Tuple3<MemberSecuritiesInfo, SecuritiesAccountInfo, MutableList<StockItemInfo>> ->
        val memberSecuritiesInfo = tuple.t1
        val securitiesAccountInfo = tuple.t2
        val stockItemInfoList = tuple.t3
        val params = createParams(securitiesAccountInfo = securitiesAccountInfo)
        val headersMap = createHeadersMap(memberSecuritiesInfo = memberSecuritiesInfo, transactionId = "TTTC8434R")

        webClient.get()
          .uri(BASE_URL + "/uapi/domestic-stock/v1/trading/inquire-balance") { builder ->
            builder.queryParams(params).build()
          }.headers { headers ->
            headersMap.forEach { (key, value) ->
              headers.set(key, value)
            }
          }
          .retrieve()
          .bodyToMono(AccountInfoResponseDto::class.java)
          .log()
          .flatMap { accountInfoResponse ->
            if (accountInfoResponse.rtCd == "0") {
              updateSecuritiesAccountInfo(securitiesAccountInfo, accountInfoResponse)
              updateStockItems(stockItemInfoList, accountInfoResponse.output1, securitiesAccountInfoId)

              Mono.just(CommonResponseDto())
            } else {
              Mono.error(RuntimeException("Error: Received message is ${accountInfoResponse.msg1}"))
            }
          }
      }
  }


  fun retrieveAccountInfo(memberId: UUID, securitiesAccountInfoId: UUID): Mono<CommonResponseDto<AccountInfoDto>> {
    val securitiesAccountInfoMono = securitiesAccountInfoRepository.findById(securitiesAccountInfoId)
    val stockItemInfoListMono = stockItemInfoRepository
      .findAllBySecuritiesAccountInfoId(securitiesAccountInfoId).collectList()

    return Mono.zip(securitiesAccountInfoMono, stockItemInfoListMono)
      .map { tuple: Tuple2<SecuritiesAccountInfo, MutableList<StockItemInfo>> ->
        CommonResponseDto(
          data = AccountInfoDto(
            securitiesAccountInfo = tuple.t1,
            stockItemInfoList = tuple.t2,
          ),
        )
      }.switchIfEmpty(Mono.error(RuntimeException("Not found")))
  }

  private fun updateSecuritiesAccountInfo(
    securitiesAccountInfo: SecuritiesAccountInfo,
    accountInfoResponse: AccountInfoResponseDto,
  ) {
    securitiesAccountInfoRepository.save(securitiesAccountInfo.copyProperties(accountInfoResponse))
  }

  private fun updateStockItems(
    stockItemInfoList: MutableList<StockItemInfo>,
    outputList: List<Output1>,
    securitiesAccountInfoId: UUID,
  ) {
    outputList.forEach { output ->
      val stockItem = stockItemInfoList.find { it.stockCode == output.pdno }
      if (stockItem != null) {
        stockItem.copyProperties(output)
      } else {
        val newStockItem = StockItemInfo(securitiesAccountInfoId, output)
        stockItemInfoList.add(newStockItem)
      }
    }
  }

  private fun createParams(securitiesAccountInfo: SecuritiesAccountInfo) = LinkedMultiValueMap<String, String>().apply {
    add("CANO", securitiesAccountInfo.encryptedAccountNumber.dropLast(2))
    add("ACNT_PRDT_CD", securitiesAccountInfo.encryptedAccountNumber.takeLast(2))
    add("AFHR_FLPR_YN", "N")
    add("OFL_YN", "")
    add("INQR_DVSN", "01")
    add("UNPR_DVSN", "01")
    add("FUND_STTL_ICLD_YN", "N")
    add("FNCG_AMT_AUTO_RDPT_YN", "N")
    add("PRCS_DVSN", "00")
    add("CTX_AREA_FK100", "")
    add("CTX_AREA_NK100", "")
  }

  private fun createHeadersMap(memberSecuritiesInfo: MemberSecuritiesInfo, transactionId: String) = mapOf(
    "Authorization" to "Bearer " + memberSecuritiesInfo.accessToken,
    "appkey" to memberSecuritiesInfo.appKey,
    "appsecret" to memberSecuritiesInfo.appSecretValue,
    "tr_id" to transactionId,
    "Accept" to "application/json",
  )

  private fun getMemberSecuritiesInfoMono(memberId: UUID) = memberSecuritiesInfoRepository.findByMemberId(memberId)
    .collectList()
    .flatMap { memberInfos ->
      if (!memberInfos.isEmpty()) {
        if (memberInfos.size == 1) {
          Mono.just(memberInfos.first())
        } else {
          Mono.error(RuntimeException("error")) // TODO EXCEPTION 처리 - 여러개의 정보가 있을때
        }
      } else {
        Mono.error(RuntimeException("error")) // TODO EXCEPTION 처리 - 정보가 없을때
      }
    }

}
