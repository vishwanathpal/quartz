package com.quartz.app.subscription.services;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quartz.app.subscription.entity.OrderRecurrence;
import com.quartz.app.subscription.repositry.OrderRecurrenceRepository;

@Service
@Transactional
public class OrderRecurrenceService {
	
	@Autowired
    private OrderRecurrenceRepository repo;
	
	 public void save(OrderRecurrence orderRecurrence) {
	        repo.save(orderRecurrence);
	    }
	 
	 public List<OrderRecurrence> listAll() {
	        return repo.findAll();
	    }
	 
}
