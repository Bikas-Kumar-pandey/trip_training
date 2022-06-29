package com.meratransport.trip.tracking;

import lombok.Data;

@Data
public class TelTracking {
	private String address;
	private String locationRetrievalStatus;
	private Location currentLocation;
	private String locationResultStatusText;
	private int locationResultStatus;

}
