// BootJar 태스크 설정: 실행 가능한 JAR 생성 비활성화
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = false
}

// Jar 태스크 설정: 일반 JAR 생성 활성화
tasks.withType<Jar> {
    enabled = true
}

val queryDslVersion = "5.0.0";

dependencies {
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // s3
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.470")

    // querydsl
    implementation("com.querydsl:querydsl-jpa:$queryDslVersion:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:$queryDslVersion:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
}