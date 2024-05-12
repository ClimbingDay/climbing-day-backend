package com.climbingday.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.climbingday.domain")
@EnableJpaRepositories("com.climbingday.domain")
public class DomainConfig {
}
