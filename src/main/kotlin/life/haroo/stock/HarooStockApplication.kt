package life.haroo.stock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["life.haroo.stock"])
class HarooStockApplication

fun main(args: Array<String>) {
  runApplication<HarooStockApplication>(*args)
}
