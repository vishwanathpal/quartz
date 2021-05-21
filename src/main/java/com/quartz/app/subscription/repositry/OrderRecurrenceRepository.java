package com.quartz.app.subscription.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quartz.app.subscription.entity.OrderRecurrence;

public interface OrderRecurrenceRepository extends JpaRepository<OrderRecurrence, Integer> {

}
