// BootJar 태스크 설정: 실행 가능한 JAR 생성 활성화
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = true
}

// Jar 태스크 설정: 일반 JAR 생성 비활성화
tasks.withType<Jar> {
    enabled = true
}

plugins {
    // asciidoctor plugin
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

// 빌드 디렉토리 참조해서 generated-snippets 폴더 지정
val snippetsDir = layout.buildDirectory.dir("generated-snippets")

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:infra"))
    implementation(project(":core:security"))

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    // valid
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // 테스트 의존성
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

// Junit5 플랫폼을 사용하여 테스트
tasks.test {
    outputs.dir(snippetsDir)
    useJUnitPlatform()
}

tasks.named<org.asciidoctor.gradle.jvm.AsciidoctorTask>("asciidoctor") {
    setSourceDir(snippetsDir.get().asFile)      // 스니펫이 저장된 디렉토리의 위치를 File 객체로 변환
    dependsOn("test")                   // test 태스크가 성공적으로 완료된 경우 실행
}