package com.meratransport.trip.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.meratransport.trip.constant.TripType;
import com.meratransport.trip.entity.Images;

import lombok.Data;import static com.meratransport.trip.constant.ApplicationConstant.*;


@Data
@JsonInclude(Include.NON_EMPTY)
public class TripResponse {

	private String id;
	
	private long sequence;
	
	private String shipperName;

	private TripType tripType;

	private String currentStatus;

	private String nextStatus;

	private String tenantId;
	
	private String transporterName;
	
	private int tagCount;
	
	private LocalDateTime startDateTime;

	private List<LocationResponse> locations;

	private List<VehicleResponse> vehicles;

	private List<DriverResponse> drivers;

	private List<Images> materialImgs;

	private List<ConsignmentResponse> consignment;

	private List<TagInfoResponse> inventory;

	private List<String> allStatus = new ArrayList<String>(Arrays.asList(TRIP_CREATED, START_INSTALLATION,
			SCAN_TAGS, ADD_MATERIAL,START_TRIP, IN_TRANSIT, SCAN_TAGS_UNLOAD, VERIFY_BARCODE, END_TRIP,TRIP_COMPLETED));
}
