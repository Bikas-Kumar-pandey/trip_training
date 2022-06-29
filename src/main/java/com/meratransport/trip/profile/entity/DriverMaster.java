package com.meratransport.trip.profile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="KEEBOOT_DRIVER")
public class DriverMaster {
	
	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name="NAME")
	private String name;

	@Column(name="SHORT_NAME")
	private String shortName;
	
	@Column(name="MOBILE_NUMBER")
	private String mobileNumber;
	
	@Column(name="TITLE")
	private String title;
	
	@Column(name="OWNERSHIP_TYPE")
	private String ownershipType;
	
	@Column(name="LANGUAGE")
	private String language;
	
	@Column(name="EMAIL_ID")
	private String emailId;
	
	@Column(name="ACTIVE")
	private boolean isActive ;
}
