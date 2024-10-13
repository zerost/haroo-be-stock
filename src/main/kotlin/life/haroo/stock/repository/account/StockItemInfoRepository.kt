package life.haroo.stock.repository.account

import life.haroo.stock.entity.account.StockItemInfo
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import java.util.UUID

interface StockItemInfoRepository : ReactiveCrudRepository<StockItemInfo, Long> {
  fun findAllBySecuritiesAccountInfoId(id: UUID): Flux<StockItemInfo>
}
