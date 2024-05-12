// BootJar 태스크 설정: 실행 가능한 JAR 생성 비활성화
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = false
}

// Jar 태스크 설정: 일반 JAR 생성 활성화
tasks.withType<Jar> {
    enabled = true
}

val mysqlVersion = "8.0.29"
val queryDslVersion = "5.0.0"


dependencies {
    // mysql
    implementation("mysql:mysql-connector-java:$mysqlVersion")
    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    // querydsl
    implementation("com.querydsl:querydsl-jpa:$queryDslVersion:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:$queryDslVersion:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
}