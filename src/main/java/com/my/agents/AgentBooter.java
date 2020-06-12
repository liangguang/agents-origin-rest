package com.my.agents;

import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.my.agents.config.AgentAutoConfiguration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootConfiguration
@ImportAutoConfiguration(value = { AgentAutoConfiguration.class })
@EnableScheduling
@EnableAutoConfiguration
@EnableSwagger2
public class AgentBooter {

	public static void main(String[] args)
			throws CompilationFailedException, IOException, InstantiationException, IllegalAccessException {
		SpringApplicationBuilder sab = new SpringApplicationBuilder(AgentBooter.class);
		sab.run(args);
	}

}
