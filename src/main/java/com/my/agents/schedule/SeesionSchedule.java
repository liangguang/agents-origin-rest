package com.my.agents.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.my.agents.service.SessionService;


@ConditionalOnProperty(prefix = "agents.session", name = "enabled", havingValue = "true", matchIfMissing = false)
@Component
public class SeesionSchedule implements InitializingBean,DisposableBean{
	
	private final Logger logger = LoggerFactory.getLogger(SeesionSchedule.class);

	@Autowired
	SessionService SessionService;
	
	@Scheduled(
			initialDelayString = "${agent.noopsession.initialDelay:5000}", //
			fixedDelayString = "${agent.noopsession.fixedDelay:30000}")
	public void execute() {
		try {
			SessionService.noopSession();
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			SessionService.setSessionID(null);
		}
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		logger.debug("启动session服务：");
	}


	@Override
	public void destroy() throws Exception {
		logger.debug("关闭获取Session服务......");
	}	
	
}
