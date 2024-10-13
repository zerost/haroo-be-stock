package life.haroo.stock.job

import life.haroo.stock.service.order.OrderService
import life.haroo.stock.utils.StockApiUtils
import org.quartz.JobExecutionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AutoStockOrderJob(
  private val orderService: OrderService
) : LoggingJob() {
  private val logger: Logger = LoggerFactory.getLogger(AutoStockOrderJob::class.java)

  override fun executeJob(context: JobExecutionContext) {
    //TODO 사용자목록을 조회해서 사용자별&계좌별로 주문을 실행해야함
    val result = orderService.purchaseStock(
      memberId = StockApiUtils.MEMBER_ID,
      securitiesAccountInfoId = StockApiUtils.SECURITIES_ACCOUNT_INFO_ID
    ).block()

    logger.info("AutoStockOrderJob result: $result")
  }
}
