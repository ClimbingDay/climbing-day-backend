package com.climbingday.infra.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.climbingday.utils.DotenvUtil;

@Configuration
public class S3Config {
	// 최상단 디렉토리 .env 파일
	Map<String, String> env = DotenvUtil.loadEnv();
	String accessKey = env.get("S3_ACCESS_KEY");
	String secretKey = env.get("S3_REFRESH_KEY");

	@Bean
	public AmazonS3 amazonS3Client() {
		return AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
			.withRegion(Regions.AP_NORTHEAST_2)
			.build();
	}
}
