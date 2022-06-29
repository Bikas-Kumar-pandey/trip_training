package com.meratransport.trip.tracking;

import lombok.Data;

@Data
public class Location {

	private double latitude;
	private double longtitude;
	private String timestamp;
	private String detailedAddress;
	
}
