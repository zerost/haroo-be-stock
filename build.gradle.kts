plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  kotlin("plugin.noarg") version "1.9.25"
  kotlin("plugin.allopen") version "1.9.25"
  kotlin("kapt") version "1.9.25"
  id("org.springframework.boot") version "3.3.4"
  id("io.spring.dependency-management") version "1.1.6"
  id("com.diffplug.spotless") version "6.25.0"
  id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

group = "life.haroo"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(17)
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.0")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.0")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
  implementation("org.springframework.boot:spring-boot-starter-quartz")
  implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.flywaydb:flyway-core:9.8.1")
  implementation("org.postgresql:postgresql:42.7.2")
  implementation("org.postgresql:r2dbc-postgresql:1.0.0.RELEASE")
  implementation("io.r2dbc:r2dbc-pool:1.0.0.RELEASE") // Ensure this is compatible
  runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.4.20-release-327")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("io.projectreactor:reactor-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll(listOf("-Xjsr305=strict"))
  }
}

spotless {
  kotlin {
    target("src/**/*.kt")
    ktlint("0.49.1")
  }
  kotlinGradle {
    target("*.gradle.kts")
    ktlint("0.49.1")
  }
}

allOpen {
  annotation("life.haroo.stock.annotation.AllOpen")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}

tasks.named("build") {
  dependsOn("spotlessKotlinApply")
}
