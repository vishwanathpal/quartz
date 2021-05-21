package com.quartz.app.subscription.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quartz.app.subscription.entity.OrderRecurrence;
import com.quartz.app.subscription.util.SchedulerUtil;

@Service
public class SchedulerService {
	
	private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
	
	@Autowired
	private final Scheduler subscriptionScheduler;
	
	boolean flag = true;

	public SchedulerService(Scheduler subscriptionScheduler) {
		this.subscriptionScheduler = subscriptionScheduler;
	}
	
	public void scheduleJob(final Class jobClasses, OrderRecurrence timerInfo) throws Exception {

		// 0 0/2 * 1/1 * ? *
		String cronEx = 0+" 0/"+timerInfo.getMinute()+" *"+" 1/1"+" *"+" ?"+" *"; 
		
		final JobDetail  jobDetails = SchedulerUtil.buildJobDetail(jobClasses, timerInfo);
		//final Trigger trigger = SchedulerUtil.buildTrigger(jobClass, timerInfo);
		final Trigger trigger = TriggerBuilder
									.newTrigger()
									.withIdentity(String.valueOf((timerInfo.getOrderId())))
									//.startAt(sDate())
									.withSchedule(CronScheduleBuilder.cronSchedule(cronEx))
									//.endAt(timerInfo.getRecurrentOrderingEndDate())
									.build();
		
		try {
			subscriptionScheduler.scheduleJob(jobDetails, trigger);
			log.error(":::::::next order scheduled::::::"+trigger.getNextFireTime());
			
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	  public void addJobDeatilsAndTimerInfo(final String timerId, final OrderRecurrence info) {
		  
		  try {
			final JobDetail jobDetail = subscriptionScheduler.getJobDetail(new JobKey(timerId));
			
			if(jobDetail == null) {
				log.error("failed find timmer with ID: "+timerId);
			}
			subscriptionScheduler.addJob(jobDetail, true, true);
			
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
		  
	  }
	
	@PostConstruct
	public void init() {
		try {
			subscriptionScheduler.start();
			subscriptionScheduler.getListenerManager().addTriggerListener(new SubscriptionTriggerListener(this));
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@PreDestroy
	public void preDestory() {
		try {
			subscriptionScheduler.shutdown();
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

}
