package com.climbingday.dto.terms;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TermsListDto {
	@NotNull(message = "클라이밍 데이 이용약관 동의는 필수 항목입니다.")
	@JsonProperty("CLIMBING_DAY_TERMS")
	private Boolean CLIMBING_DAY_TERMS;

	@NotNull(message = "위치 이용약관 동의는 필수 항목입니다.")
	@JsonProperty("LOCATION_TERMS")
	private Boolean LOCATION_TERMS;

	// 필드 이름으로 값을 반환
	public Boolean getTerm(String type) {
		try {
			Field field = this.getClass().getDeclaredField(type);
			field.setAccessible(true);
			return (Boolean) field.get(this);
			// 지정된 이름의 필드를 찾을 수 없거나 접근할 수 없는 필드의 경우
		}catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Map<String, Boolean> getFieldnamesAndValues() {
		Map<String, Boolean> fieldMap = new HashMap<>();
		Field[] fields = this.getClass().getDeclaredFields();

		for(Field field: fields) {
			if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
				field.setAccessible(true); // private 필드에 접근할 수 있도록 설정
				try {
					// 필드 값 가져오기
					Boolean value = (Boolean) field.get(this);
					fieldMap.put(field.getName(), value);
				} catch (IllegalAccessException e) {
					// 접근 오류 처리
					e.printStackTrace();
				}
			}
		}

		return fieldMap;
	}

}
