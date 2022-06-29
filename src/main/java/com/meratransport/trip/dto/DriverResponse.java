package com.meratransport.trip.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DriverResponse {
	
	private String id;

	private Driver driver;
	
	private boolean isActive;

	private String status;

	private String changeReason;

}
