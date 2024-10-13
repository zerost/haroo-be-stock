package life.haroo.stock.config

import life.haroo.stock.job.AutoStockOrderJob
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.TriggerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.scheduling.quartz.SpringBeanJobFactory

@Configuration
class QuartzConfig {
  @Bean
  fun schedulerFactoryBean(): SchedulerFactoryBean {
    return SchedulerFactoryBean().apply {
      setJobFactory(SpringBeanJobFactory())
    }
  }

  @Bean
  fun jobDetail() = JobBuilder.newJob(AutoStockOrderJob::class.java)
    .withIdentity("autoStockOrderJob")
    .build()

  @Bean
  fun trigger(jobDetail: JobDetail) = TriggerBuilder.newTrigger()
    .forJob(jobDetail)
    .withIdentity("autoStockOrderTrigger")
    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 6 ? * MON-FRI"))
    .build()
}
