package life.haroo.stock.utils

import life.haroo.stock.entity.account.MemberSecuritiesInfo
import java.util.UUID

class StockApiUtils {
  companion object {
    val MEMBER_ID = UUID.fromString("0ad9acba-d99d-4f74-8784-1a4a62e21689") // TODO 파라미터로 받아야함
    val SECURITIES_ACCOUNT_INFO_ID = UUID.fromString("ef9cad63-d059-4033-b16f-5f586257bd82") // TODO 파라미터로 받아야함


    fun createHeadersMap(memberSecuritiesInfo: MemberSecuritiesInfo, transactionId: String): Map<String, String> {
      return mapOf(
        "Authorization" to "Bearer " + memberSecuritiesInfo.accessToken,
        "appkey" to memberSecuritiesInfo.appKey,
        "appsecret" to memberSecuritiesInfo.appSecretValue,
        "tr_id" to transactionId,
        "Accept" to "application/json",
      )
    }
  }
}
