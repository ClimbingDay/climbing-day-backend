plugins {
	java
	war
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.asciidoctor.jvm.convert") version "3.3.2" apply true
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

val asciidoctorExt = configurations.create("asciidoctorExt") {
	extendsFrom(configurations["testImplementation"])
}

allprojects {
	repositories {
		mavenCentral()
	}
}

subprojects {
	group = "com.climbingday"

	val snippetsDir = layout.buildDirectory.dir("generated-snippets")
	val testContainerVersion = "1.19.5"

	// 하위 프로젝트 플러그인 적용
	apply(plugin = "java")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.asciidoctor.jvm.convert")
	apply(plugin = "java-test-fixtures") // plugin 설정이 있어야 testFixtures 디렉토리를 정상적으로 인지함.

	dependencies {
		// Spring Boot 기본 스타터 의존성
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-starter-web")

		// asciidoctorj
		implementation("org.asciidoctor:asciidoctorj:2.4.3")

		// Lombok 의존성 설정
		implementation("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
		testAnnotationProcessor("org.projectlombok:lombok")

		// testContainer
		testImplementation("org.testcontainers:junit-jupiter:$testContainerVersion")
	}

	tasks.test {
		outputs.dir(snippetsDir)
		useJUnitPlatform()
	}

	tasks.named<org.asciidoctor.gradle.jvm.AsciidoctorTask>("asciidoctor") {
		setSourceDir(snippetsDir.get().asFile)
		dependsOn("test")
	}
}

val collectSnippets by tasks.registering(Copy::class) {
	from(subprojects.map { it.layout.buildDirectory.dir("generated-snippets") })
	into(layout.buildDirectory.dir("central-snippets"))
}

tasks.register<org.asciidoctor.gradle.jvm.AsciidoctorTask>("asciidoctorAll") {
	dependsOn(collectSnippets)

//	 명시적으로 AsciidoctorJ 확장을 설정
	extensions.create<org.asciidoctor.gradle.jvm.AsciidoctorJExtension>("asciidoctorj", objects)

	setSourceDir(layout.buildDirectory.dir("central-snippets").get().asFile)
	inputs.dir(layout.buildDirectory.dir("central-snippets"))
	setOutputDir(layout.buildDirectory.dir("docs").get().asFile)
}