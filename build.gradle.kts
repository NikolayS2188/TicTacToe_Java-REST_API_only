plugins {
    java
    id("org.springframework.boot") version "3.1.5" // Spring Boot плагин для сборки fat JAR
    id("io.spring.dependency-management") version "1.1.5" // Управление версиями зависимостей из Spring BOM

}

group = "com.karkaror"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starter Web - включает Spring MVC, Tomcat, Jackson и т.д.
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Database - для Spring Boot 3.x используем актуальные версии
    implementation("org.postgresql:postgresql:42.7.2")

    // Для работы с JSON в Hibernate 6.x используем hypersistence-utils
    implementation("io.hypersistence:hypersistence-utils-hibernate-62:3.7.0")

    // JWT (JJWT) — генерация и валидация токенов
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // Тестовые зависимости
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8" // Правильная обработка кириллицы
    options.release.set(21) // Оптимизация под JDK 21
}

tasks.test {
    useJUnitPlatform() // Использовать JUnit Platform для запуска тестов
    maxParallelForks = Runtime.getRuntime().availableProcessors() // Параллельный запуск тестов
}

springBoot {
    mainClass.set("com.karkaror.tictactoe.TicTacToeApplication")
}
