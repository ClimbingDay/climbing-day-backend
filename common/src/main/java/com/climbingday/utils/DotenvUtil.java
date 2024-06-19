package com.climbingday.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DotenvUtil {
	public static Map<String, String> loadEnv() {
		Map<String, String> envMap = new HashMap<>();
		String absolutePath = Paths.get(System.getProperty("user.dir"), ".env").normalize().toString();

		try (Stream<String> stream = Files.lines(Paths.get(absolutePath))) {
			stream.filter(line -> !line.startsWith("#") && line.contains("="))
				.forEach(line -> {
					int index = line.indexOf("=");
					String key = line.substring(0, index).trim();
					String value = line.substring(index + 1).trim();
					envMap.put(key, value);
				});
		} catch (IOException e) {
			e.printStackTrace();
		}

		return envMap;
	}
}
