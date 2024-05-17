package com.climbingday.infra.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestConfig {
	private static final String SQL_USER_NAME = "root";
	private static final String SQL_PASSWORD = "1234";
	private static final String SQL_DATABASE_NAME = "test";


	@Container
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8")
		.withUsername(SQL_USER_NAME)
		.withPassword(SQL_PASSWORD)
		.withDatabaseName(SQL_DATABASE_NAME);

	@DynamicPropertySource
	static void databaseProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
	}
}
