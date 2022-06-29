package com.meratransport.trip.dto;

import java.util.List;

import lombok.Data;

@Data
public class DriverRequest {
	private String id;

	private String name;

	private String shortName;

	private String mobileNumber;

	private String title;

	private String ownershipType;

	private String language;

	private String emailId;
	
	List<String> imgs;

	private boolean isActive = true;

}
