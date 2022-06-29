package com.meratransport.trip.dto;

import java.util.List;

import lombok.Data;

@Data
public class VehicleRequest {
	
	private String id;

	private String vehicleType;

	private String ownershipType;

	private String vehicleNumber;

	private String vendorId;

	private String vendorName;

	private List<String> imgs;

}
