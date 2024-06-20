import java.nio.file.Files
import java.nio.file.Paths

plugins {
	java
	war
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}

buildscript {
	// 빌드 스크립트 의존성을 위한 저장소
	repositories {
		mavenCentral()
	}
}

// 최상위 레벨에서 저장소 설정
allprojects {
	repositories {
		mavenCentral()
	}
}

tasks.withType<JavaCompile> {
	sourceCompatibility = JavaVersion.VERSION_21.toString()
	targetCompatibility = JavaVersion.VERSION_21.toString()
}

val asciidoctorExt = configurations.create("asciidoctorExt") {
	extendsFrom(configurations["testImplementation"])
}

val dotenvPath = Paths.get(rootProject.rootDir.absolutePath, ".env")
if (Files.exists(dotenvPath)) {
	val dotenv = Files.readAllLines(dotenvPath)
		.filter { it.contains("=") }
		.map { it.split("=") }
		.associate { it[0] to it[1] }

	allprojects {
		dotenv.forEach { (key, value) ->
			project.extra[key] = value
		}

		tasks.withType<Test> {
			dotenv.forEach { (key, value) ->
				environment(key, value)
			}
		}
	}
}

subprojects {
	group = "com.climbingday"

	val testContainerVersion = "1.19.5"

	// 하위 프로젝트 플러그인 적용
	apply(plugin = "java")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "java-test-fixtures") // plugin 설정이 있어야 testFixtures 디렉토리를 정상적으로 인지함.

	dependencies {
		// Spring Boot 기본 스타터 의존성
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-starter-web")

		// Jasypt 의존성 추가
		implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4")

		// asciidoctorj
		implementation("org.asciidoctor:asciidoctorj:2.4.3")

		// Lombok 의존성 설정
		implementation("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
		testAnnotationProcessor("org.projectlombok:lombok")

		// testContainer
		testImplementation("org.testcontainers:junit-jupiter:$testContainerVersion")
	}
}

/**
 * all 테스트 및 api 문서 생성
 */
tasks.register<Copy>("allGeneratedDocs") {
	dependsOn(":api:member:openapi3")
	dependsOn(":api:center:openapi3")

	val memberBuildDir = project(":api:member").layout.buildDirectory
	val centerBuildDir = project(":api:center").layout.buildDirectory

	// member
	from(memberBuildDir.file("api-spec/openapi3.yaml")) {
		rename { fileName ->
			fileName.replace("openapi3.yaml", "member-openapi3.yaml")
		}
	}
	into(project(":api:gateway").file("src/main/resources/static"))

	// center
	from(centerBuildDir.file("api-spec/openapi3.yaml")){
		rename { fileName ->
			fileName.replace("openapi3.yaml", "center-openapi3.yaml")
		}
	}
	into(project(":api:gateway").file("src/main/resources/static"))
}

/**
 * member 테스트 및 api 문서 생성
 */
tasks.register<Copy>("memberGeneratedDocs") {
	dependsOn(":api:member:openapi3")

	val memberBuildDir = project(":api:member").layout.buildDirectory

	// member
	from(memberBuildDir.file("api-spec/openapi3.yaml")) {
		rename { fileName ->
			fileName.replace("openapi3.yaml", "member-openapi3.yaml")
		}
	}
	into(project(":api:gateway").file("src/main/resources/static"))
}

/**
 * center 테스트 및 api 문서 생성
 */
tasks.register<Copy>("centerGeneratedDocs") {
	dependsOn(":api:center:openapi3")

	val centerBuildDir = project(":api:center").layout.buildDirectory

	// center
	from(centerBuildDir.file("api-spec/openapi3.yaml")){
		rename { fileName ->
			fileName.replace("openapi3.yaml", "center-openapi3.yaml")
		}
	}
	into(project(":api:gateway").file("src/main/resources/static"))
}