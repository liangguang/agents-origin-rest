package com.my.agents.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MyServiceConfig {

	@Value("${web.url:}")
	public String WEB_NEED_URL; 
	
	@Value("${user.name:}")
	public String NEED_USER_NAME;
	
	@Value("${user.password:}")
	public String NEED_USER_PWD ;

	@Value("${time.out:600000L}")
	public String NEED_TIME_OUT;

	public String getWebUrl() {
		String url = WEB_NEED_URL;
		assertPassParameter(url, WEB_NEED_URL);
		return url;
	}
	public String getNEEDUserName(){
		return NEED_USER_NAME;
	}
	public String getNEEDUserPwd(){
		return NEED_USER_PWD;
	}
	
	public long getSeesionTimeout(){
		long timeout = 600000L;
		String time = System.getProperty(NEED_TIME_OUT);
		if(StringUtils.hasLength(time)){
			timeout = Long.parseLong(time);
		}
		return timeout;
	}
	
	
	private static void assertPassParameter(String param, String key) {
		if (StringUtils.isEmpty(param)) {
			throw new IllegalStateException("请配置" + key + "参数!");
		}
	}
	
	public static void main(String[] args) {
		
	}
}
