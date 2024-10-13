package life.haroo.stock.repository.account

import life.haroo.stock.entity.account.SecuritiesAccountInfo
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import java.util.UUID

interface SecuritiesAccountInfoRepository : ReactiveCrudRepository<SecuritiesAccountInfo, UUID>
