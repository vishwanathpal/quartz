package com.quartz.app.subscription.services;

import java.text.MessageFormat;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTriggerHistoryPlugin implements SchedulerPlugin, TriggerListener {

	private String name;

	private String triggerFiredMessage = "Trigger {1}.{0} fired job {6}.{5} at: {4, date, HH:mm:ss MM/dd/yyyy}";

	private String triggerMisfiredMessage = "Trigger {1}.{0} misfired job {6}.{5}  at: {4, date, HH:mm:ss MM/dd/yyyy}.  Should have fired at: {3, date, HH:mm:ss MM/dd/yyyy}";

	private String triggerCompleteMessage = "Trigger {1}.{0} completed firing job {6}.{5} at {4, date, HH:mm:ss MM/dd/yyyy} with resulting trigger instruction code: {9}";

	private final Logger log = LoggerFactory.getLogger(getClass());

	public LoggingTriggerHistoryPlugin() {
	}

	protected Logger getLog() {
		return log;
	}

	public String getTriggerFiredMessage() {
		return triggerFiredMessage;
	}

	public void setTriggerFiredMessage(String triggerFiredMessage) {
		this.triggerFiredMessage = triggerFiredMessage;
	}

	public String getTriggerMisfiredMessage() {
		return triggerMisfiredMessage;
	}

	public void setTriggerMisfiredMessage(String triggerMisfiredMessage) {
		this.triggerMisfiredMessage = triggerMisfiredMessage;
	}

	public String getTriggerCompleteMessage() {
		return triggerCompleteMessage;
	}

	public void setTriggerCompleteMessage(String triggerCompleteMessage) {
		this.triggerCompleteMessage = triggerCompleteMessage;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {

		 if (!getLog().isInfoEnabled()) {
	            return;
	        } 
	        
	        Object[] args = {
	            trigger.getKey().getName(), trigger.getKey().getGroup(),
	            trigger.getPreviousFireTime(), trigger.getNextFireTime(),
	            new java.util.Date(), context.getJobDetail().getKey().getName(),
	            context.getJobDetail().getKey().getGroup(),
	            Integer.valueOf(context.getRefireCount())
	        };

	        getLog().info(MessageFormat.format(getTriggerFiredMessage(), args));
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		return false;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {

		 if (!getLog().isInfoEnabled()) {
	            return;
	        } 
	        
	        Object[] args = {
	            trigger.getKey().getName(), trigger.getKey().getGroup(),
	            trigger.getPreviousFireTime(), trigger.getNextFireTime(),
	            new java.util.Date(), trigger.getJobKey().getName(),
	            trigger.getJobKey().getGroup()
	        };

	        getLog().info(MessageFormat.format(getTriggerMisfiredMessage(), args));
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {

		 if (!getLog().isInfoEnabled()) {
	            return;
	        } 
	        
	        String instrCode = "UNKNOWN";
	        if (triggerInstructionCode == CompletedExecutionInstruction.DELETE_TRIGGER) {
	            instrCode = "DELETE TRIGGER";
	        } else if (triggerInstructionCode == CompletedExecutionInstruction.NOOP) {
	            instrCode = "DO NOTHING";
	        } else if (triggerInstructionCode == CompletedExecutionInstruction.RE_EXECUTE_JOB) {
	            instrCode = "RE-EXECUTE JOB";
	        } else if (triggerInstructionCode == CompletedExecutionInstruction.SET_ALL_JOB_TRIGGERS_COMPLETE) {
	            instrCode = "SET ALL OF JOB'S TRIGGERS COMPLETE";
	        } else if (triggerInstructionCode == CompletedExecutionInstruction.SET_TRIGGER_COMPLETE) {
	            instrCode = "SET THIS TRIGGER COMPLETE";
	        }

	        Object[] args = {
	            trigger.getKey().getName(), trigger.getKey().getGroup(),
	            trigger.getPreviousFireTime(), trigger.getNextFireTime(),
	            new java.util.Date(), context.getJobDetail().getKey().getName(),
	            context.getJobDetail().getKey().getGroup(),
	            Integer.valueOf(context.getRefireCount()),
	            triggerInstructionCode.toString(), instrCode
	        };

	        getLog().info(MessageFormat.format(getTriggerCompleteMessage(), args));
	}

	@Override
	public void initialize(String pname, Scheduler scheduler, ClassLoadHelper loadHelper) throws SchedulerException {

		this.name = pname;
		scheduler.getListenerManager().addTriggerListener(this, EverythingMatcher.allTriggers());
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
