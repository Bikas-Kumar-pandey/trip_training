package com.meratransport.trip.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class TripsResponse {
	
	private long sequence;
	
	private String tripId;
	
	private String pickUplocation;
	
	private String dropLocation;
	
	private LocalDateTime startDateTime;
	
	private String vehicleNumber;
	private String vehicleId;
	private String vehicleType;
	
	private String tripStatus;
	
	private String consignment;
	private String goodsType;
	
	private String shipperName;
	
	private String driverId;
	private String driverNumber;
	private String driverName;
	
	private String transporterName;
	
	
	

}
