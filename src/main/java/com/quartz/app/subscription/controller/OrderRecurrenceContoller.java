package com.quartz.app.subscription.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
