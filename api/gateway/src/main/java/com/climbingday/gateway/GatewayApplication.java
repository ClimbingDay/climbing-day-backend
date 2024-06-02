package com.climbingday.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder,
		@Value("${member-service-url}") String memberServiceUrl,
		@Value("${mail-service-url}") String mailServiceUrl
	) {
		return builder.routes()
			.route("api-member", r -> r.path("/v1/member/**")
				.uri(memberServiceUrl))
			.route("api-email", r -> r.path("/v1/email/**")
				.uri(mailServiceUrl))
			.build();
	}
}
