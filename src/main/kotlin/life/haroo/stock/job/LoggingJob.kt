package life.haroo.stock.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

abstract class LoggingJob : Job {
  private val logger: Logger = LoggerFactory.getLogger(LoggingJob::class.java)

  override fun execute(context: JobExecutionContext) {
    val startTime = LocalDateTime.now()
    logger.info("Job started at: $startTime")

    try {
      executeJob(context)
    } catch (e: Exception) {
      logger.error("Job failed with exception: ${e.message}", e)
    } finally {
      val endTime = LocalDateTime.now()
      val duration = ChronoUnit.MILLIS.between(startTime, endTime)

      logger.info("Job completed")
      logger.info("Job finished at: $endTime, Duration: ${duration}ms")
    }
  }


  protected abstract fun executeJob(context: JobExecutionContext)
}
