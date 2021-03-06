package com.quartz.app.subscription.services;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quartz.app.subscription.entity.OrderRecurrence;
import com.quartz.app.subscription.job.OrderRecurrenceJobDetails;

@Service
public class SchedulerTimerService {

	@Autowired
	private final SchedulerService schedulerService;

	public SchedulerTimerService(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}
	
	public void subscriptionOrder(OrderRecurrence timerInfo) throws Exception {
		
		//final OrderRecurrence timerInfo = new OrderRecurrence();
		//timerInfo.setNumberOfTimesToRecurrence("5");
		//timerInfo.setRepeatInterval(2000);
		//timerInfo.setInitialOffset(1000);
		//timerInfo.setCallBackData("customer subscription data");
		
		schedulerService.scheduleJob(OrderRecurrenceJobDetails.class, timerInfo);
	}

	public boolean deleteTimer(String timmerId) {
		return schedulerService.deleteTrigger(timmerId);
	}
	
	public String pauseTrigger(final String triggerId) throws SchedulerException {
		return schedulerService.pauseTrigger(triggerId);
		
	}

	public String resumeTrigger(final String triggerId) throws SchedulerException {
		return schedulerService.pauseTrigger(triggerId);
	}

	public String unscheduledJob(String triggerId) throws SchedulerException {
		return schedulerService.unschedulTrigger(triggerId);
	}

	public String scheduledJob(String triggerId) throws SchedulerException {
		return schedulerService.schedulTrigger(triggerId);
	}
	
	public void reScheduleSubscription(OrderRecurrence timerInfo) throws Exception {
		schedulerService.reSchedulTrigger(OrderRecurrenceJobDetails.class, timerInfo);
	}

	
}
