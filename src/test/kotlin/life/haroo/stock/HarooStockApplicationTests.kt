package life.haroo.stock

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class HarooStockApplicationTests {
  @Autowired private lateinit var objectMapper: ObjectMapper

  @Test
  fun contextLoads() {
  }

  @Test
  fun Test() {
//    val dto = Token2ResponseDto(
//      accessToken = "sampleAccessToken",
//      accessTokenTokenExpired = LocalDateTime.of(2024, 10, 7, 10, 30, 12),
//      tokenType = "Bearer",
//      expiresIn = 3600,
//    )
//
//    val jsonString = objectMapper.writeValueAsString(dto)
//    println("Serialized JSON: $jsonString")
//
//    val deserializedDto = objectMapper.readValue(jsonString, Token2ResponseDto::class.java)
//    println("Deserialized DTO: $deserializedDto")
  }
}
