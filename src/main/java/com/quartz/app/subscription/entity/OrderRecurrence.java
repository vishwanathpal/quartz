package com.quartz.app.subscription.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
 
@Entity
@Table(name = "order_recurrence_schedule")
public class OrderRecurrence implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private Integer orderId;
	
	@Column
	private String minute;
	
	@Column
	private String hour;
	
	@Column
	private String day;
	
	@Column
	private String month;
	
	@Column
	private String weekDay;
	
	@Column(name = "number_of_times_to_recur")
	private String numberOfTimesToRecurrence;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", locale = "en_GB")
	@Temporal(TemporalType.DATE)
	@Column(name = "recurrent_ordering_end_date")
	private Date recurrentOrderingEndDate;
	
	@Column(name = "is_test_record")
	private boolean isTestRecord;
	
	@Column
	private boolean locked;
	
	@Column
	private Integer userCreated;
	
	@Column
	private Integer userEdited;
	
	@Column
	private Timestamp createdDatetime;
	
	@Column
	private Timestamp updatedDatetime;
	
	 public OrderRecurrence() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public String getNumberOfTimesToRecurrence() {
		return numberOfTimesToRecurrence;
	}

	public void setNumberOfTimesToRecurrence(String numberOfTimesToRecurrence) {
		this.numberOfTimesToRecurrence = numberOfTimesToRecurrence;
	}

	public Date getRecurrentOrderingEndDate() {
		return recurrentOrderingEndDate;
	}

	public void setRecurrentOrderingEndDate(Date recurrentOrderingEndDate) {
		this.recurrentOrderingEndDate = recurrentOrderingEndDate;
	}

	public boolean isTestRecord() {
		return isTestRecord;
	}

	public void setTestRecord(boolean isTestRecord) {
		this.isTestRecord = isTestRecord;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public Integer getUserCreated() {
		return userCreated;
	}

	public void setUserCreated(Integer userCreated) {
		this.userCreated = userCreated;
	}

	public Integer getUserEdited() {
		return userEdited;
	}

	public void setUserEdited(Integer userEdited) {
		this.userEdited = userEdited;
	}

	public Timestamp getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Timestamp createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public Timestamp getUpdatedDatetime() {
		return updatedDatetime;
	}

	public void setUpdatedDatetime(Timestamp updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}

	@Override
	public String toString() {
		return "OrderRecurrence [id=" + id + ", orderId=" + orderId + ", minute=" + minute + ", hour=" + hour + ", day="
				+ day + ", month=" + month + ", weekDay=" + weekDay + ", numberOfTimesToRecurrence="
				+ numberOfTimesToRecurrence + ", recurrentOrderingEndDate=" + recurrentOrderingEndDate
				+ ", isTestRecord=" + isTestRecord + ", locked=" + locked + ", userCreated=" + userCreated
				+ ", userEdited=" + userEdited + ", createdDatetime=" + createdDatetime + ", updatedDatetime="
				+ updatedDatetime + "]";
	}
	
}
