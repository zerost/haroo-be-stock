package life.haroo.stock.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
  info = Info(
    title = "Haroo Stock API",
    description = """
이 API는 주식 자동 정보 조회 및 자동 주문 기능을 제공합니다. 사용자는 계좌 정보를 조회하고, 주식 주문을 수행하며, 토큰을 저장할 수 있습니다. 또한, 다양한 주식 항목에 대한 정보를 얻을 수 있습니다.
    """,
    version = "0.0.1",
    summary = "API for stock information retrieval and automatic trading",
  ),
)
class SwaggerConfig
