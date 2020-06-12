package com.my.agents.service;

import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.my.agents.model.GetAccessTokenResponse;

@Component
public class PaasAccessTokenSupport {
	
	private static Map<String, Date> times = new HashMap<String, Date>();
	private static Map<String, GetAccessTokenResponse> responses = new HashMap<String, GetAccessTokenResponse>();
	private static final String GET_TOKEN_URL = "/auth/token/getAccessToken/";
	private static final int LIMIT = 120;// 两个小时

	public String getAccessToken(String userID) {
		
		if (times.get(userID) != null && responses.get(userID) != null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -(LIMIT - 20));
			if (cal.getTime().before(times.get(userID))) {
				return responses.get(userID).getCode();
			}
		}
		RestTemplate template = new RestTemplate();
		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		template.setRequestFactory(clientFactory);
		String uriStr = GET_TOKEN_URL;
		URI uri = UriComponentsBuilder.fromUriString(uriStr) //
				.queryParam("timeStamp", new Date().getTime()) //
				.build().encode().toUri();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		@SuppressWarnings({ "rawtypes", "unchecked" })
		HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<GetAccessTokenResponse> responseEntity = template.exchange(uri, HttpMethod.POST, requestEntity,
				GetAccessTokenResponse.class);
		GetAccessTokenResponse body = responseEntity.getBody();
		if ("0".equals(body.getCode())) {
			responses.put(userID, body);
			times.put(userID, new Date());
		} else {
			throw new IllegalStateException(
					"获取accessToken失败, code = " + body.getCode() + ", desc = " + body.getMessage());
		}
		return body.getCode();
	}

	public static void main(String[] args) {
		
	}
}