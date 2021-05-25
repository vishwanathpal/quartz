package com.quartz.app.subscription.controller;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.quartz.app.subscription.entity.OrderRecurrence;
import com.quartz.app.subscription.services.OrderRecurrenceService;
import com.quartz.app.subscription.services.SchedulerTimerService;

@RestController
public class OrderRecurrenceContoller {

	@Autowired
	private final OrderRecurrenceService service;

	@Autowired
	private final SchedulerTimerService schedulerTimerService;

	public OrderRecurrenceContoller(OrderRecurrenceService service, SchedulerTimerService schedulerTimerService) {
		this.service = service;
		this.schedulerTimerService = schedulerTimerService;
	}

	@PostMapping("/subscribe")
	public void add(@RequestBody OrderRecurrence orderRecurrence) throws Exception {
		service.save(orderRecurrence);
		schedulerTimerService.subscriptionOrder(orderRecurrence);

	}

	@GetMapping("/subscribe")
	public List<OrderRecurrence> list() {
		return service.listAll();
	}

	@DeleteMapping("/subscribe/{timerId}")
	public boolean deleteTimmer(@PathVariable String timerId) {
		return schedulerTimerService.deleteTimer(timerId);
	}

	// pause trigger by trigger id (don't use it)
	@GetMapping("/subscribe/pause/{triggerId}")
	public String pauseTrigger(@PathVariable String triggerId) throws SchedulerException {
		return schedulerTimerService.pauseTrigger(triggerId);
	}

	// resume trigger by trigger id (don't use it)
	@GetMapping("/subscribe/resume/{triggerId}")
	public String resumeTrigger(@PathVariable String triggerId) throws SchedulerException {
		return schedulerTimerService.resumeTrigger(triggerId);
	}

	// Unscheduled job by trigger id
	@GetMapping("/subscribe/unscheduled/{triggerId}")
	public String unscheduledJob(@PathVariable String triggerId) throws SchedulerException {
		return schedulerTimerService.unscheduledJob(triggerId);
	}

	// scheduled job by trigger id
	@GetMapping("/subscribe/scheduled/{triggerId}")
	public String scheduledJob(@PathVariable String triggerId) throws SchedulerException {
		return schedulerTimerService.scheduledJob(triggerId);
	}
	
	@PostMapping("/subscribe/re-schedule")
	public void reScheduleSubscription(@RequestBody OrderRecurrence orderRecurrence) throws Exception {
		service.save(orderRecurrence);
		schedulerTimerService.reScheduleSubscription(orderRecurrence);

	}
}
