package com.meratransport.trip.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.meratransport.trip.entity.TripConsignmentLocation;
import com.meratransport.trip.entity.TripDevicesLocation;
import com.meratransport.trip.entity.TripExpenses;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class LocationResponse {

	private String id;

	private Location location;

	private String serialNo;

	private String activity;

	private LocalDateTime activityTime;

	private LocalDateTime inTime ;

	private LocalDateTime outTime;

	private String serviceTime;

	private TripDevicesLocation devicesLocation;

	private TripConsignmentLocation consignmentLocation;

	private TripExpenses expense;

}
