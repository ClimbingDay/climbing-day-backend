package com.climbingday.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NullToEmptyStringSerializer extends JsonSerializer<String> {

	@Override
	public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws
		IOException {
		if(value == null) {
			jsonGenerator.writeString("");
		}else {
			jsonGenerator.writeString(value);
		}
	}
}
