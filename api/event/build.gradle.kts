// BootJar 태스크 설정: 실행 가능한 JAR 생성 활성화
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = true
}

// Jar 태스크 설정: 일반 JAR 생성 비활성화
tasks.withType<Jar> {
    enabled = true
}

dependencies {
    implementation(project(":core:infra"))
    implementation(project(":core:security"))
    implementation(project(":common"))

    // valid
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // smtp
    implementation("org.springframework.boot:spring-boot-starter-mail:3.0.5")
}