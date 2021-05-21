package com.quartz.app.subscription.component;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.quartz.app.subscription.entity.OrderRecurrence;
import com.quartz.app.subscription.services.OrderRecurrenceService;
import com.quartz.app.subscription.services.SchedulerTimerService;
/*
@Component
public class DBExecution {
	
	private static final Logger log = LoggerFactory.getLogger(DBExecution.class);
	
	@Autowired
    private final OrderRecurrenceService service;
	
	@Autowired
	private final SchedulerTimerService schedulerTimerService;
	
	public DBExecution(OrderRecurrenceService service, SchedulerTimerService schedulerTimerService) {
		this.service = service;
		this.schedulerTimerService = schedulerTimerService;
	}
	
	@Scheduled(cron = "0 0/2 * 1/1 * ?")
	//@Scheduled(fixedRate = 120000)
	public void getOrderData() throws Exception {
		log.info(":::: Schedular Executed, interval of 2min fetching data from DB :::: "+new Date());
		List<OrderRecurrence> list = service.listAll();
		schedulerTimerService.subscriptionOrder(list);
	}
}
*/