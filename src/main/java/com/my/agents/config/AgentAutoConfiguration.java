package com.my.agents.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ComponentScan({ "com.my.agents.service", 
	"com.my.agents.schedule",
	"com.my.agents.rs"})
public class AgentAutoConfiguration {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public Docket docketCommon() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Agents").select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(new ApiInfoBuilder().
						title("Agents Restful API").
						description("Agents接口的API").
						contact(new Contact("Agents", "", "")).
						version("1.0").
						build());
	}
	
}
