package com.quartz.app.subscription.services;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.quartz.listeners.TriggerListenerSupport;
import com.quartz.app.subscription.entity.OrderRecurrence;

public class SubscriptionTriggerListener extends TriggerListenerSupport	{
	
	private final SchedulerService schedulerService;
	
	public SubscriptionTriggerListener(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}

	@Override
	public String getName() {
		return SubscriptionTriggerListener.class.getSimpleName();
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
	
		final String timmerId = trigger.getKey().getName();
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		OrderRecurrence jobDetails = (OrderRecurrence) jobDataMap.get(timmerId);
		schedulerService.addJobDeatilsAndTimerInfo(timmerId, jobDetails);
	}

}
