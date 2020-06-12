package com.my.agents.utils;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String ObjToJson(Object s) throws IOException {
		return objectMapper.writeValueAsString(s);

	}

	public static <T> T JsonToObj(String jason, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		return objectMapper.readValue(jason, clazz);

	}

	public static <T> T JsonToObj(InputStream jason, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		return objectMapper.readValue(jason, clazz);

	}

}
