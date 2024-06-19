package com.climbingday.infra.config;

import java.util.Map;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.climbingday.utils.DotenvUtil;

@Configuration
public class JasyptConfig {

	@Bean
	public StringEncryptor stringEncryptor() {
		// 최상단 디렉토리 .env 파일
		Map<String, String> env = DotenvUtil.loadEnv("../../../../../../../../../.env"); // 실제 경로에 맞게 조정 필요
		String password = env.get("JASYPT_ENCRYPTOR_PASSWORD");
		String algorithm = env.get("JASYPT_ALGORITHM");

		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(password);
		encryptor.setAlgorithm(algorithm);
		return encryptor;
	}
}
