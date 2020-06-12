package com.my.agents.service;


import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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

@Component
public class SessionService {
	private static final Logger logger = LoggerFactory
			.getLogger(SessionService.class);

	private static String sessionID = null;
	
	private static String logonUserId = null;
	
	private static Map<String, Long> map = new HashMap<String,Long>();

	private static final String LOGON_URL = "/logon";
	
	private static final String LOGOFF_URL = "/logoff";
	
	private static final String NOOPSESSION_URL = "/noop/session";
	
	@Autowired
	MyServiceConfig MyServiceConfig;
	
	public String logon() {
		
		String userName = MyServiceConfig.getNEEDUserName(); 
		
		String password = MyServiceConfig.getNEEDUserPwd();
		
		String json = "{\"userId\":\""+userName+"\",\"password\":\""+password+"\",\"LogonType\":\"UserID\"}";
		
		RestTemplate template = new RestTemplate();
		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		template.setRequestFactory(clientFactory);
		
		String urlStr = MyServiceConfig.getWebUrl()+ LOGON_URL;
		
		URI uri = UriComponentsBuilder.fromUriString(urlStr).build().encode().toUri();
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> requestEntity = new HttpEntity<String>(json,requestHeaders);
		
		ResponseEntity<String> responseEntity = template.exchange(uri, HttpMethod.POST,
				requestEntity,String.class);
		sessionID = responseEntity.getBody();
		map.put(sessionID,new Date().getTime());
		return responseEntity.getBody();
	}

	public void logoff() {
		
		String urlStr = MyServiceConfig.getWebUrl()+ LOGOFF_URL; 
		
		URI uri = UriComponentsBuilder.fromUriString(urlStr)
				  .queryParam("sid", sessionID).build().encode().toUri();
		
		RestTemplate template = new RestTemplate();
		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		template.setRequestFactory(clientFactory);
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
		
		ResponseEntity<?> responseEntity = 
					template.exchange(uri, HttpMethod.POST,requestEntity,Object.class);
		
		logger.debug("退出操作：status:" + responseEntity.getStatusCode());
	}

	public void noopSession() {
		
		if(sessionID == null){
			logon();
		}else{
			long logonTime = map.get(sessionID);
			if(new Date().getTime() - logonTime > MyServiceConfig.getSeesionTimeout()){
				logon();
			}else{
				map.put(sessionID,new Date().getTime());
				//实际 不需要请求了
				String urlStr = MyServiceConfig.getWebUrl()+ NOOPSESSION_URL;
				URI uri = UriComponentsBuilder.fromUriString(urlStr)
						  .queryParam("sid", sessionID).build().encode().toUri();
				
				RestTemplate template = new RestTemplate();
				ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
				template.setRequestFactory(clientFactory);
				
				HttpHeaders requestHeaders = new HttpHeaders();
				requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
				HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
				template.exchange(uri, HttpMethod.POST,requestEntity,Object.class);
			}
		}
	}

	public void setSessionID(String sessionID) {
		SessionService.sessionID = sessionID;
	}

	public static String getSessionID() {
		return sessionID;
	}
	
	public static String getLogonUserId() {
		return logonUserId;
	}

	public static void setLogonUserId(String logonUserId) {
		SessionService.logonUserId = logonUserId;
	}
	
	public static Map<String, Long> getMap() {
		return map;
	}

	public static void main(String[] args) throws InterruptedException {}
	
	
}
