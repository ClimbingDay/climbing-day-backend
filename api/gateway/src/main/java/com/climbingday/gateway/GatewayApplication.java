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
		@Value("${center-service-url}") String centerServiceUrl,
		@Value("${mail-service-url}") String mailServiceUrl
	) {
		return builder.routes()
			.route("api-member", r -> r.path("/v1/member/**")
				.or()
				.path("/v1/crew/**")
				.or()
				.path("/v1/general/**")
				.uri(memberServiceUrl))
			.route("api-center", r -> r.path("/v1/center/**")
				.uri(centerServiceUrl))
			.route("api-mail", r -> r.path("/v1/mail/**")
				.uri(mailServiceUrl))
			.build();
	}
}
