package com.my.agents.service;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.agents.model.Task;

@Component
public class TaskService {
	private final Logger logger = LoggerFactory.getLogger(TaskService.class);

	@Autowired
	MyServiceConfig MyServiceConfig;
	
	public List<Task> queryTasks(String hostName) throws Exception {

		RestTemplate template = new RestTemplate();
		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		template.setRequestFactory(clientFactory);

		String urlStr = MyServiceConfig.getWebUrl();
		logger.debug("查询任务：请求地址=" + urlStr);
		URI uri = UriComponentsBuilder.fromUriString(urlStr)
				.queryParam("hostName", hostName).build().encode().toUri();

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		requestHeaders.add(HttpHeaders.COOKIE, "sid=" + SessionService.getSessionID());
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);

		ResponseEntity<String> responseEntity = template.exchange(uri, HttpMethod.GET, requestEntity, String.class);
		logger.debug("查询任务 response:" + responseEntity.getBody());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);

		List<Task> entity = objectMapper.readValue(responseEntity.getBody(),
				new TypeReference<List<Task>>() {
				});

		return entity;
	}
	
	public Task getTask(String hostName)  throws Exception {
		RestTemplate template = new RestTemplate();
		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		template.setRequestFactory(clientFactory);

		String urlStr = MyServiceConfig.getWebUrl();
		logger.debug("查询任务：请求地址=" + urlStr);
		URI uri = UriComponentsBuilder.fromUriString(urlStr)
				.queryParam("hostName", hostName).build().encode().toUri();

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		requestHeaders.add(HttpHeaders.COOKIE, "sid=" + SessionService.getSessionID());
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);

		ResponseEntity<String> responseEntity = template.exchange(uri, HttpMethod.GET, requestEntity, String.class);
		logger.debug(" response:" + responseEntity.getBody());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);

		Task entity = null;
		
		if(responseEntity.getBody() !=null ){
			entity = objectMapper.readValue(responseEntity.getBody(),Task.class);
		}

		return entity;
	}

	public Task getTaskById(String workId) throws Exception {

		RestTemplate template = new RestTemplate();
		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		template.setRequestFactory(clientFactory);

		String urlStr = MyServiceConfig.getWebUrl();
		logger.debug("查询指定工作项任务：请求地址=" + urlStr);
		URI uri = UriComponentsBuilder.fromUriString(urlStr).queryParam("workId", workId).build().encode().toUri();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		requestHeaders.add(HttpHeaders.COOKIE, "sid=" + SessionService.getSessionID());
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<String> responseEntity = template.exchange(uri, HttpMethod.GET, requestEntity, String.class);
		logger.debug("查询指定工作项任务===getWorkItem response:" + responseEntity.getBody());
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		Task entity = objectMapper.readValue(responseEntity.getBody(), new TypeReference<Task>() {
		});
		return entity;
	}
	
	//根据流程实例查询相关数据
		public Map<String, Object> getRelevantDatasByProId(String procId) throws JsonParseException, JsonMappingException, IOException{

			RestTemplate template = new RestTemplate();
			ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
			template.setRequestFactory(clientFactory);
			
			String urlStr = MyServiceConfig.getWebUrl();
			
			URI uri = UriComponentsBuilder.fromUriString(urlStr)
					.queryParam("procId", procId)
					.build().encode().toUri();

			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
			requestHeaders.add(HttpHeaders.COOKIE, "sid=" + SessionService.getSessionID());
			@SuppressWarnings({ "unchecked", "rawtypes" })
			HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
			/*
			ResponseEntity<Map<String, Object>> responseEntity = template.exchange(uri, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<Map<String, Object>>() {
					});
			*/
			ResponseEntity<String> responseEntity = template.exchange(uri, HttpMethod.GET,
					requestEntity,String.class);
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_EMPTY);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = objectMapper.readValue(responseEntity.getBody(), Map.class);
			
			return map;
		}

	public static void main(String[] args) {
	}

}
