package com.meratransport.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripRequest {
	
	private LocationRequest pickUpLocation;
	
	private LocationRequest dropLocation;
	
	private VehicleRequest vehicle;
	
	private DriverRequest driver;
	
	private ConsignmentRequest consignment;
	
	private String transporterName;
	

}
