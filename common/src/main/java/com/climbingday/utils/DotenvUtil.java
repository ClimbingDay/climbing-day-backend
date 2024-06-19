package com.climbingday.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DotenvUtil {
	public static Map<String, String> loadEnv(String filePath) {
		Map<String, String> envMap = new HashMap<>();
		try {
			Files.lines(Paths.get(filePath))
				.filter(line -> !line.startsWith("#") && line.contains("="))
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
