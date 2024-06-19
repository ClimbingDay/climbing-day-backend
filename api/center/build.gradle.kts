// BootJar 태스크 설정: 실행 가능한 JAR 생성 활성화
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = true
}

// Jar 태스크 설정: 일반 JAR 생성 비활성화
tasks.withType<Jar> {
    enabled = true
}

// 빌드 디렉토리 참조해서 generated-snippets 폴더 지정
val snippetsDir = layout.buildDirectory.dir("generated-snippets")
val restdocsApiSpecVersion = "0.18.2"
val openUiVersion = "1.7.0"

plugins {
    id("com.epages.restdocs-api-spec") version "0.17.1"
}

openapi3 {
    setServer("http://localhost:8080")
    title = "Climbing-day-member-swagger API Documentation"
    description = "Spring REST Docs with SwaggerUI"
    version = "0.0.1"
    format = "yaml"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:infra"))
    implementation(project(":core:security"))
    implementation(project(":common"))
    implementation(testFixtures(project(":core:infra")))

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // valid
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // s3
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.470")

    // 테스트 의존성
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:$restdocsApiSpecVersion")
    testImplementation("com.epages:restdocs-api-spec-restassured:$restdocsApiSpecVersion")
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")
}

// 스니펫 문서 정리
val cleanGeneratedSnippets by tasks.registering(Delete::class) {
    val snippetsDir = project(":api:center").layout.buildDirectory.dir("generated-snippets")
    delete(snippetsDir)
}

// Junit5 플랫폼을 사용하여 테스트
tasks.test {
    dependsOn(cleanGeneratedSnippets)

    jvmArgs("-Xshare:off")          // CDS 비활성화
    outputs.dir(file(snippetsDir))
    useJUnitPlatform()
}