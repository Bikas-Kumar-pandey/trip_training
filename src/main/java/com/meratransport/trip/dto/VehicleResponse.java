package com.meratransport.trip.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class VehicleResponse {

	private String id;
	
	private Vehicle vehicle;

	private boolean isActive;

	private String status;

	private String changeReason;

}
