package com.quartz.app.subscription.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.utils.DirtyFlagMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quartz.app.subscription.entity.OrderRecurrence;
import com.quartz.app.subscription.job.OrderRecurrenceJobDetails;
import com.quartz.app.subscription.util.SchedulerUtil;

@Service
public class SchedulerService {
	
	private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
	
	@Autowired
	private final Scheduler subscriptionScheduler;
	
	//LoggingTriggerHistoryPlugin loggingTriggerHistoryPlugin;
	
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
									//.withSchedule(CronScheduleBuilder.cronSchedule(cronEx).withMisfireHandlingInstructionFireAndProceed())
									//.withSchedule(CronScheduleBuilder.cronSchedule(cronEx).withMisfireHandlingInstructionDoNothing())
									.withSchedule(CronScheduleBuilder.cronSchedule(cronEx).withMisfireHandlingInstructionIgnoreMisfires())
									//.endAt(timerInfo.getRecurrentOrderingEndDate())
									.build();
		
		// null with new post request
		Trigger oldTrigger = subscriptionScheduler.getTrigger(trigger.getKey());
		//true = job exist and false = new job
		//boolean  oldjobDetails = subscriptionScheduler.checkExists(trigger.getJobKey());
		
		//new trigger to schedule
		if(oldTrigger == null) {
			try {
				subscriptionScheduler.scheduleJob(jobDetails, trigger);
				log.error(":::::::next order scheduled::::::"+trigger.getNextFireTime());
				
			} catch (SchedulerException e) {
				log.error(e.getMessage(), e);
			}
		}
		// existing one to update, trigger and scheduler
		else {
			TriggerBuilder tb = oldTrigger.getTriggerBuilder();
			String cronExsNew = 0+" 0/"+timerInfo.getMinute()+" *"+" 1/1"+" *"+" ?"+" *"; 
			Trigger newTrigger = tb.withSchedule(CronScheduleBuilder.cronSchedule(cronEx).withMisfireHandlingInstructionIgnoreMisfires())
				    .build();
			
			//newTrigger.getJobKey();
			//subscriptionScheduler.getJobDetail(jobDetails.getKey());
			
			try {
				subscriptionScheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
				subscriptionScheduler.addJob(jobDetails, true);
				//subscriptionScheduler.scheduleJob(jobDetails, newTrigger);
				
				// Add the new job to the scheduler, instructing it to "replace"
				//  the existing job with the given name and group (if any)
				/*
				JobDetail job1 = (JobDetail) JobBuilder
				        		 .newJob(OrderRecurrenceJobDetails.class)
				        		 .withIdentity(newTrigger.getJobKey())
				        		 .build();

				// store, and set overwrite flag to 'true'     
									subscriptionScheduler.addJob(job1, true);
				*/
				log.error(":::::::Updated order scheduled::::::"+newTrigger.getNextFireTime());
				
			} catch (SchedulerException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	// adding job and trigger in DB
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
	  
	  // delete job and trigger
	  public boolean deleteTrigger(final String timmerId) {
		  try {
			return subscriptionScheduler.deleteJob(new JobKey(timmerId));
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	  }
	  
	  //pause trigger
	  public String pauseTrigger(final String triggerId) throws SchedulerException {
		  
		  boolean isTriggerExist = subscriptionScheduler.checkExists(TriggerKey.triggerKey(triggerId));
		  log.info("isvabailable trigger:::::"+isTriggerExist);
		  
		  if(isTriggerExist) {
			  subscriptionScheduler.pauseTrigger(TriggerKey.triggerKey(triggerId));
			  return "trigger has paused with ID::"+ triggerId;
		  }
		  else
			  return "trigger with ID::"+triggerId+" does not exist";
		}
	  
	  //resume trigger => after resume, need to 
	  public String resumeTrigger(final String triggerId) throws SchedulerException {
		  
		  boolean isTriggerExist = subscriptionScheduler.checkExists(TriggerKey.triggerKey(triggerId));
		  log.info("isvabailable trigger:::::"+isTriggerExist);
		  
		  if(isTriggerExist) {
			  subscriptionScheduler.resumeTrigger(TriggerKey.triggerKey(triggerId));
			  subscriptionScheduler.resumeJob(new JobKey(triggerId));
			  return "trigger has resumed with ID::"+ triggerId;
		  }
		  else
			  return "trigger with ID::"+triggerId+" does not exist";
		}
	  
	  //Unscheduling a Particular Trigger of Job (stop execution)
	  public String unschedulTrigger(final String triggerId) throws SchedulerException {
		  
		  boolean isTriggerExist = subscriptionScheduler.checkExists(TriggerKey.triggerKey(triggerId));
		  log.info("isvabailable trigger:::::"+isTriggerExist);
		  
		  if(isTriggerExist) {
			  subscriptionScheduler.unscheduleJob(TriggerKey.triggerKey(triggerId));
			  return "trigger has unschedule with ID::"+ triggerId;
		  }
		  else
			  return "trigger with ID::"+triggerId+" does not exist";
		}
	  
	  //Scheduling an already stored job (start execution with previous time and date mean same job details)
	  public String schedulTrigger(final String triggerId) throws SchedulerException {
		  
		  final JobDetail jobDetail = subscriptionScheduler.getJobDetail(new JobKey(triggerId));
		  OrderRecurrence orderRec = null;
		  
		  for (Entry<String, Object> entry : jobDetail.getJobDataMap().entrySet()) { 
				  orderRec = (OrderRecurrence) entry.getValue();
				  break;
			  }
		  
		  String cronEx = 0+" 0/"+orderRec.getMinute()+" *"+" 1/1"+" *"+" ?"+" *";
		  
		  final Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity(String.valueOf(triggerId))
					.withSchedule(CronScheduleBuilder.cronSchedule(cronEx).withMisfireHandlingInstructionIgnoreMisfires())
				    .forJob(triggerId)
				    .build();

				// Schedule the trigger
		  subscriptionScheduler.scheduleJob(trigger);
		  return "Trigger started with ID:: "+triggerId;
		}
	  
	  // reschedule job with new cron and unschedule previous trigger and create new one
	  public String reSchedulTrigger(final Class jobClasses, OrderRecurrence timerInfo) throws SchedulerException {
		  
		  //final JobDetail  jobDetails = SchedulerUtil.buildJobDetail(jobClasses, timerInfo);
		  String cronEx = 0+" 0/"+timerInfo.getMinute()+" *"+" 1/1"+" *"+" ?"+" *";
		  final JobDetail jobDetail = subscriptionScheduler.getJobDetail(new JobKey(String.valueOf(timerInfo.getOrderId())));
		  boolean isTriggerExist = subscriptionScheduler.checkExists(TriggerKey.triggerKey(String.valueOf(timerInfo.getOrderId())));
		  
		  if(jobDetail != null && isTriggerExist) {
			  subscriptionScheduler.deleteJob(new JobKey(String.valueOf(timerInfo.getOrderId())));
			  //subscriptionScheduler.unscheduleJob(TriggerKey.triggerKey(String.valueOf(timerInfo.getOrderId())));
			  
			  final JobDetail  jobDetails = SchedulerUtil.buildJobDetail(jobClasses, timerInfo);
			  
			  final Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity(String.valueOf(String.valueOf(timerInfo.getOrderId())))
						.withSchedule(CronScheduleBuilder.cronSchedule(cronEx).withMisfireHandlingInstructionIgnoreMisfires())
					    .forJob(jobDetails)
					    .build();
			  
			  subscriptionScheduler.scheduleJob(jobDetails, trigger);
			  return "Trigger started with ID:: "+timerInfo.getOrderId();
		  }else
			  return "invalid trigger id:: "+timerInfo.getOrderId();
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
