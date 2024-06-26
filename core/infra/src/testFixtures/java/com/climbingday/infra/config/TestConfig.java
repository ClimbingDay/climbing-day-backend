package com.climbingday.infra.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
public class TestConfig {
	// database
	private static final String SQL_USER_NAME = "root";
	private static final String SQL_PASSWORD = "1234";
	private static final String SQL_DATABASE_NAME = "test";

	// redis
	private static final int REDIS_PORT = 6379;

	@Container
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8")
		.withUsername(SQL_USER_NAME)
		.withPassword(SQL_PASSWORD)
		.withDatabaseName(SQL_DATABASE_NAME)
		.withCopyFileToContainer(MountableFile.forClasspathResource("db/initdb.d/1-schema.sql"), "/docker-entrypoint-initdb.d/1-schema.sql")
		.withCopyFileToContainer(MountableFile.forClasspathResource("db/initdb.d/2-data.sql"), "/docker-entrypoint-initdb.d/2-data.sql");

	@Container
	static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.2-alpine")
		.withExposedPorts(REDIS_PORT);

	@DynamicPropertySource
	static void databaseProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
	}

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		redisContainer.start();
		registry.add("spring.data.redis.host", redisContainer::getHost);
		registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(REDIS_PORT));
	}
}
