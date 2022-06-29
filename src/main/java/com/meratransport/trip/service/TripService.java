package com.meratransport.trip.service;

import java.util.List;
import java.util.Map;

import com.meratransport.trip.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

public interface TripService {
	
	TripResponse createTrip(TripRequest request, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception;

	TripResponse getTrip(String tripId, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception;

	TripResponse addMaterialInfo(MaterialRequest materialRequest, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception;

	boolean changeTripStatus(StatusUpdate statusUpdate, Map<String, String> headers) throws Exception;

	List<TripsResponse> getAllTrip(Pageable pageable, Map<String, String> headers, String vehicleNumber, HttpHeaders httpHeaders) throws Exception;

	TripResponse updateTrip(String tripId, TripRequest request, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception;

	TripResponse getTripStatus(String tripId, Map<String, String> headers) throws Exception;

	boolean deleteTrip(String tripId, Map<String, String> headers) throws Exception;

	boolean addInventoryToTrip(TripInventory inventory, Map<String, String> prepareHeaderMap) throws Exception;

	public boolean addDriver(String tripId, DriverRequest driverRequest, Map<String, String> headers) throws Exception;
}
