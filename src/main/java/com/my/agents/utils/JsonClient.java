package com.my.agents.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonClient {
	private static ObjectMapper mapper = new ObjectMapper();
	{
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
				false);
		mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
				false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
	}

	private static void addHeader(HttpURLConnection conn,
			Map<String, String> headerMap) {
		if (headerMap != null) {
			for (String key : headerMap.keySet()) {
				conn.addRequestProperty(key, headerMap.get(key));
			}
		}
	}

	public static <T> T postObject(String url, Object object,
			Class<T> responseClass, Object... args)
			throws JsonMappingException, IOException {
		return postObject(url, object, false, responseClass, args);
	}

	public static <T> T postObject(String url, Object object,
			boolean urlEncoded, Class<T> responseClass, Object... args)
			throws JsonParseException, JsonMappingException, IOException {
		return postObject(url, object, urlEncoded, null, responseClass, args);
	}

	public static <T> T postObject(String url, Object object,
			Map<String, String> headerMap, Class<T> responseClass,
			Object... args) throws JsonParseException, JsonMappingException,
			IOException {
		return postObject(url, object, false, headerMap, responseClass, args);
	}

	public static <T> T postObject(String url, Object object,
			boolean urlEncoded, Map<String, String> headerMap,
			Class<T> responseClass, Object... args) throws JsonParseException,
			JsonMappingException, IOException {
		try {
			url = String.format(url, args);
		} catch (Exception e) {

		}
		URL oUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) oUrl.openConnection();
		conn.setRequestProperty("Content-type",
				"application/json;charset=UTF-8");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		addHeader(conn, headerMap);
		if (urlEncoded) {
			String s = mapper.writeValueAsString(object);
			s = URLEncoder.encode(s, "UTF-8");
			try (OutputStreamWriter osw = new OutputStreamWriter(
					conn.getOutputStream())) {
				osw.write(s);
				osw.flush();
			}
		} else {
			mapper.writeValue(conn.getOutputStream(), object);
		}
		conn.getOutputStream().flush();
		return mapper.readValue(conn.getInputStream(), responseClass);
	}

	public static <T> T postObjectMap(String url,
			Map<String, String> headerMap, Map<String, String> payload,
			Class<T> responseClass, Object... args) throws JsonParseException,
			JsonMappingException, IOException {
		try {
			url = String.format(url, args);
		} catch (Exception e) {

		}
		URL oUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) oUrl.openConnection();
		conn.setRequestProperty("Content-type",
				"application/json;charset=UTF-8");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		addHeader(conn, headerMap);
		String linker = "";
		try (OutputStreamWriter osw = new OutputStreamWriter(
				conn.getOutputStream())) {
			for (String key : payload.keySet()) {
				osw.write(linker);
				String s = payload.get(key);
				osw.write(key);
				osw.write("=");
				s = URLEncoder.encode(s, "UTF-8");
				osw.write(s);
				linker = "&";
			}
			osw.flush();
		}
		conn.getOutputStream().flush();
		return mapper.readValue(conn.getInputStream(), responseClass);
	}

	public static <T> T putObject(String url, Object object,
			Class<T> responseClass, Object... args) throws JsonParseException,
			JsonMappingException, IOException {
		return putObject(url, object, null, responseClass, args);
	}

	public static <T> T putObject(String url, Object object,
			Map<String, String> headerMap, Class<T> responseClass,
			Object... args) throws JsonParseException, JsonMappingException,
			IOException {
		try {
			url = String.format(url, args);
		} catch (Exception e) {

		}
		URL oUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) oUrl.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		addHeader(conn, headerMap);
		conn.setRequestMethod("PUT");
		mapper.writeValue(conn.getOutputStream(), object);
		conn.getOutputStream().flush();
		return mapper.readValue(conn.getInputStream(), responseClass);
	}

	public static <T> T getObject(String url, Class<T> responseClass,
			Object... args) throws JsonParseException, JsonMappingException,
			IOException {
		return getObject(url, responseClass, null, args);
	}

	public static <T> T getObject(String url, Class<T> responseClass,
			Map<String, String> headerMap, Object... args)
			throws JsonParseException, JsonMappingException, IOException {
		try {
			url = String.format(url, args);
		} catch (Exception e) {

		}
		URL oUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) oUrl.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(false);
		addHeader(conn, headerMap);
		return mapper.readValue(conn.getInputStream(), responseClass);
	}

	public static <T> T getObjectWithParam(String url, Class<T> responseClass,
			Map<String, String> headerMap, Map<String, String> params,
			Object... args) throws JsonParseException, JsonMappingException,
			IOException {
		if (url == null) {
			throw new IOException("URL cannot be null");
		}
		try {
			url = String.format(url, args);
		} catch (Exception e) {

		}
		if (params != null) {
			url = url.replaceAll("[\\?\\&]$", "");
			StringBuilder sb = new StringBuilder(url);
			String linker = url.contains("?") ? "&" : "?";
			for (String key : params.keySet()) {
				sb.append(linker);
				sb.append(key);
				sb.append("=");
				sb.append(URLEncoder.encode(params.get(key), "UTF-8"));
				linker = "&";
			}
		}

		URL oUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) oUrl.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(false);
		addHeader(conn, headerMap);
		return mapper.readValue(conn.getInputStream(), responseClass);
	}

	public static <T> T deleteObject(String url, Class<T> responseClass,
			Object... args) throws JsonParseException, JsonMappingException,
			IOException {
		return deleteObject(url, responseClass, null, args);
	}

	public static <T> T deleteObject(String url, Class<T> responseClass,
			Map<String, String> headerMap, Object... args)
			throws JsonParseException, JsonMappingException, IOException {
		try {
			url = String.format(url, args);
		} catch (Exception e) {

		}
		URL oUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) oUrl.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(false);
		addHeader(conn, headerMap);
		conn.setRequestMethod("PUT");
		return mapper.readValue(conn.getInputStream(), responseClass);
	}

}
