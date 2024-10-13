package life.haroo.stock.entity.account

import life.haroo.stock.dto.account.Token2ResponseDto
import life.haroo.stock.utils.DateUtils
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

@Table(name = "member_securities_info")
data class MemberSecuritiesInfo(
  @Id
  val memberSecuritiesInfoId: Long,
  val memberId: UUID,
  val securitiesCode: String,
  val appKey: String,
  val appSecretValue: String,
  val accessToken: String,
  val accessTokenExpiresIn: ZonedDateTime,
  val createdAt: ZonedDateTime = ZonedDateTime.now(),
  val updatedAt: ZonedDateTime = ZonedDateTime.now(),
  val createdBy: String,
  val updatedBy: String,
) {
  constructor() : this(
    memberSecuritiesInfoId = 0,
    memberId = UUID.randomUUID(),
    securitiesCode = "",
    appKey = "",
    appSecretValue = "",
    accessToken = "",
    accessTokenExpiresIn = ZonedDateTime.now(),
    createdAt = ZonedDateTime.now(),
    updatedAt = ZonedDateTime.now(),
    createdBy = "",
    updatedBy = "",
  )
  fun copyProperties(tokenResponse: Token2ResponseDto): MemberSecuritiesInfo {
    return this.copy(
      accessToken = tokenResponse.accessToken,
      accessTokenExpiresIn = tokenResponse.accessTokenTokenExpired.atZone(ZoneId.of("Asia/Seoul")) ?: DateUtils
        .MIN_TIMESTAMP.atZone(ZoneId.of("UTC")),
    )
  }
}
