package com.climbingday.infra.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

	@Bean
	public StringEncryptor stringEncryptor() {
		// 최상단 디렉토리 .env 파일
		String password = System.getenv("JASYPT_ENCRYPTOR_PASSWORD");
		String algorithm = System.getenv("JASYPT_ALGORITHM");

		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(password);
		encryptor.setAlgorithm(algorithm);
		return encryptor;
	}
}
