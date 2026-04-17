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

    // Тестовые зависимости
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0")) // JUnit 5 BOM
    testImplementation("org.junit.jupiter:junit-jupiter") // JUnit 5 API и движок
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") // Запуск тестов на JUnit Platfor
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8" // Правильная обработка кириллицы
    options.release.set(18) // Оптимизация под JDK 18
}

tasks.test {
    useJUnitPlatform() // Использовать JUnit Platform для запуска тестов
    maxParallelForks = Runtime.getRuntime().availableProcessors() // Параллельный запуск тестов
}