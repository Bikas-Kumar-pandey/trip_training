package com.meratransport.trip.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class DeviceLog {
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	
	private String serialNo;
	
	private String type;
	
	private String provider;
	
	private String user;
	
	private String tenantId;
	
	private String status;
	
	private String source;
	
	private String destination;
	
	private String direction;
	
	@ManyToOne
	private Trip trip;
	
	@ManyToOne
	@JoinColumn(name="device_id")
	private DeviceInfo deviceInfo;

}
