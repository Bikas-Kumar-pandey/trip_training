package com.meratransport.trip.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.meratransport.trip.entity.Images;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class Vehicle {

	private String vehicleId;

	private String vehicleType;
	
	private String vehicleNumber;

	private String ownershipType;

	private String vendorName;

	private String vendorId;
	
	private List<Images> imgs;

}
