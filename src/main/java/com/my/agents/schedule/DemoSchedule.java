package com.my.agents.schedule;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.agents.model.Task;
import com.my.agents.service.TaskService;

/**
 * 一个定时任务的demo
 */
@ConditionalOnProperty(prefix = "agents.demo", name = "enabled", havingValue = "true", matchIfMissing = false)
@Component
public class DemoSchedule implements InitializingBean, DisposableBean {

	private final Logger logger = LoggerFactory.getLogger(DemoSchedule.class);

	private final static ObjectMapper objectMapper = new ObjectMapper();

	@Value("${service.defName:测送任务}")
	private String defName;

	@Autowired
	public TaskService _taskService;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Set<String> defNameSet = new HashSet<String>();

	@Override
	public void afterPropertiesSet() throws Exception {
		defNameSet.add(defName);
		logger.debug("启动查询{}：", defName);
	}

	@Scheduled(initialDelayString = "${agent.task.initialDelay:5000}", //
			fixedDelayString = "${agent.task.fixedDelay:30000}")
	public void execute() throws Throwable {

		logger.debug("正在查询{}任务", defName);

		Task task = _taskService.getTask("");
		
		try {
			while (task != null) {
				logger.debug("查询到{}任务详情:{}", defName,objectMapper.writeValueAsString(task));
				doWork("");
			}
		} catch (Throwable e) {
			//do something
		}
		 task = _taskService.getTask("");
	}

	private void doWork(String moId) throws Throwable {
		
	}

	@Override
	public void destroy() throws Exception {
		logger.debug("关闭服务......");
	}

}
