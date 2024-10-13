package life.haroo.stock.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class JacksonConfig {
  @Bean
  @Primary
  fun jackson2ObjectMapperBuilder(): ObjectMapper {
    val javaTimeModule = JavaTimeModule()

    return Jackson2ObjectMapperBuilder()
      .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
      .modules(javaTimeModule)
      .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
      .defaultViewInclusion(true)
      .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
      .build()
  }
}
