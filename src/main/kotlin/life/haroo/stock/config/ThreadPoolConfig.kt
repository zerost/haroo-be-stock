package life.haroo.stock.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class ThreadPoolConfig {
  @Bean
  fun taskExecutor(): TaskExecutor {
    val executor = ThreadPoolTaskExecutor()
    executor.corePoolSize = 20
    executor.maxPoolSize = 20
    executor.setQueueCapacity(500)
    executor.setThreadNamePrefix("JpaExecutor-")
    executor.initialize()
    return executor
  }
}
