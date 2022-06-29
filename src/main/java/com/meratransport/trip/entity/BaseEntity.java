package com.meratransport.trip.entity;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {
	
	private Date createdTime = new Date();
	
	private Date lastModifiedTime;
	
	private String lastModifiedUserId;
	
	private String createdUserId;
	
	private Long entityVersion;
	
	private String appType;	
	
	private String extraInfo;
	

}
