package com.my.agents.rs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="代码发布接口",tags= {"Agents API"})
@RestController
@RequestMapping(value="/api/agents")
public class AgentsRS {
	
	public static Logger logger = LoggerFactory.getLogger(AgentsRS.class);

	@ApiOperation(value="测试")
	@ResponseBody
	@RequestMapping(value="/test",method = {RequestMethod.GET})
	public String testWork(){
		logger.debug("日志级别：DEBUG");
		logger.info("日志级别：INFO");
		logger.error("日志级别：ERROR");
		return "it is work!";
		
	}
}
