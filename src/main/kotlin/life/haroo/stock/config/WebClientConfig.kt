package life.haroo.stock.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfig {
  @Bean
  fun webClient(objectMapper: ObjectMapper): WebClient {
    val connectionProvider = ConnectionProvider.builder("custom")
      .maxConnections(50)
      .pendingAcquireTimeout(Duration.ofMillis(5000))
      .build()

    val httpClient = HttpClient.create(connectionProvider)
      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
      .doOnConnected { conn ->
        conn.addHandlerLast(ReadTimeoutHandler(30, TimeUnit.SECONDS))
        conn.addHandlerLast(WriteTimeoutHandler(30, TimeUnit.SECONDS))
      }

    return WebClient.builder()
      .codecs { configurer: ClientCodecConfigurer ->
        configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
        configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
      }
      .clientConnector(ReactorClientHttpConnector(httpClient))
      .filter(logRequest())
      .filter(logResponse())
      .build()
  }

  fun logRequest(): ExchangeFilterFunction {
    return ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
      println("Request: ${clientRequest.method()} ${clientRequest.url()}")
      clientRequest.headers().forEach { name, values ->
        values.forEach { value -> println("$name: $value") }
      }
      Mono.just(clientRequest)
    }
  }

  fun logResponse(): ExchangeFilterFunction {
    return ExchangeFilterFunction.ofResponseProcessor { clientResponse ->
      println("Response: ${clientResponse.statusCode()}")
      clientResponse.headers().asHttpHeaders().forEach { name, values ->
        values.forEach { value -> println("$name: $value") }
      }
      Mono.just(clientResponse)
    }
  }
}
