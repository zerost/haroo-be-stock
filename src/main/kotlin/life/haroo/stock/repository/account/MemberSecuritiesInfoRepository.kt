package life.haroo.stock.repository.account

import life.haroo.stock.entity.account.MemberSecuritiesInfo
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import java.util.UUID

interface MemberSecuritiesInfoRepository : ReactiveCrudRepository<MemberSecuritiesInfo, Long> {
  fun findByMemberId(memberId: UUID): Flux<MemberSecuritiesInfo>
}
