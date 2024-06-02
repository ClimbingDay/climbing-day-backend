// BootJar 태스크 설정: 실행 가능한 JAR 생성 활성화
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = true
}

// Jar 태스크 설정: 일반 JAR 생성 비활성화
tasks.withType<Jar> {
    enabled = true
}

configurations {
    compileOnly {
        // web 의존성 제외
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-web")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:4.1.4")
}
