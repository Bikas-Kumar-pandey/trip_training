package com.meratransport.trip.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class TripDevices extends BaseEntity {
	
	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;
	
	//Master Table Reference
	private String deviceId;
	
	private String deviceType;

	@ManyToOne
	private Trip trip;

	private boolean isActive;

	private String status;

	private String changeReason;

}
