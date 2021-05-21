package com.quartz.app.subscription.util;

import java.util.Date;
import java.util.List;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import com.quartz.app.subscription.entity.OrderRecurrence;

public class SchedulerUtil {

	public SchedulerUtil() {}
	
	public static JobDetail buildJobDetail(final Class jobClass, OrderRecurrence timerInfo) {
		
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(String.valueOf(timerInfo.getOrderId()), timerInfo);
		
		return JobBuilder
				.newJob(jobClass)
				.withIdentity(String.valueOf(timerInfo.getOrderId()))
				.setJobData(jobDataMap)
				.build();
	}
	
	// not being used
	public static Trigger buildTrigger(Class jobClass, OrderRecurrence timerInfo) {
		
		SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
										.withIntervalInMilliseconds(Integer.parseInt(timerInfo.getNumberOfTimesToRecurrence()));
		if(false) {
			builder = builder.repeatForever();
		}else {
			builder = builder.withRepeatCount(Integer.parseInt(timerInfo.getNumberOfTimesToRecurrence())-1);
		}
		
		return TriggerBuilder
				.newTrigger()
				.withIdentity(jobClass.getSimpleName())
				.withSchedule(builder)
				.startAt(new Date(System.currentTimeMillis() +0))
				.build();
	}

}
