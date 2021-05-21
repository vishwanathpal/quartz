package com.quartz.app.subscription.job;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PersistJobDataAfterExecution
public class OrderRecurrenceJobDetails implements Job {

	private static final Logger log = LoggerFactory.getLogger(OrderRecurrenceJobDetails.class);

	/*
	 * Method will be call every time based on the scheduled time. It contains all
	 * the information about job to be executed.
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		Object jobDetails = jobDataMap.get(context.getJobDetail().getKey().toString().substring(8));

		log.info(":::::subscription order details excuted on date :"+new Date()+":::" +jobDetails);
	}
}
