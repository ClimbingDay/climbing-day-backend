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

tasks.withType<JavaCompile> {
	sourceCompatibility = JavaVersion.VERSION_21.toString()
	targetCompatibility = JavaVersion.VERSION_21.toString()
}


allprojects {
	repositories {
		maven {
			url = uri("https://maven.oracle.com")
			credentials {
				username = "seongo0521@gmail.com"
				password = "A1148721as@"
			}
		}
		mavenCentral()
	}
}

subprojects {
	group = "com.climbingday"

	// 하위 프로젝트 플러그인 적용
	apply(plugin = "java")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		// Spring Boot 기본 스타터 의존성
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-starter-web")

		// Lombok 의존성 설정
		implementation("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
		testAnnotationProcessor("org.projectlombok:lombok")

		// 테스트 의존성
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}

	// JUnit 플랫폼을 사용한 테스트 구성
	tasks.test {
		useJUnitPlatform()
	}
}