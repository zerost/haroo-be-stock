# 주식 관리 시스템

## 개요

이 프로젝트는 Kotlin, Spring Boot, WebFlux를 사용하여 구축된 주식 관리 시스템입니다. 주식 계좌를 관리하고 자동 주식을 구매하는 기능 제공합니다.

## 사전 준비

- JDK 17
- Gradle
- Postgresql

## 시작하기
### 저장소 클론
```bash
git clone https://github.com/xrootone/stock-management-system.git
cd stock-management-system
```

### 프로젝트 빌드
```bash
./gradlew build
```

### 애플리케이션 실행
```bash
./gradlew bootRun
```

## Endpoints
### 계좌 관리
- 계좌 정보 저장  
  - URL: /api/v1/stock/account/stock-account  
  - Method: POST  
  - 설명: 회원과 증권 계좌에 대한 계좌 정보를 저장합니다.  
- 토큰 저장 
  - URL: /api/v1/stock/account/token
  - Method: POST
  - 설명: 회원에 대한 증권 토큰을 저장합니다.
- 계좌 정보 조회  
  - URL: /api/v1/stock/account/stock-account
  - Method: GET
  - 설명: 회원과 증권 계좌에 대한 계좌 정보를 조회합니다.
  
### 주문관리
- 주식 구매
  - URL: /api/v1/stock/order/stock-orders
  - Method: POST
  - 설명: 회원과 증권 계좌에 대한 주식을 구매합니다.

### CronJob
이 프로젝트는 주기적으로 주식 정보를 업데이트와 주식자동주문을 하기 위해 QuartzScheduler를 사용합니다.  
스케쥴링은 'QuartzConfig' 클래스에서 설정할 수 있습니다.

### `QuartzConfig.kt`
```kotlin
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
```
Quartz 설정은 application.yml 파일에서 구성할 수 있습니다.  
### `application.yml` 
```yaml
spring:
  quartz:
    job-store-type: memory
    properties:
      org:
        quartz:
          scheduler:
            instanceName: QuartzScheduler
          threadPool:
            threadCount: 5
          jobStore:
            misfireThreshold: 60000
```


## 의존성
- Kotlin
- Spring Boot
- WebFlux
- Gradle
- Postgresql
- Quartz
- flyway

