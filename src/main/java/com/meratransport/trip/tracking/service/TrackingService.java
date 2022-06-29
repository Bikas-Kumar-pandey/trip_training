package com.meratransport.trip.tracking.service;

import java.util.Map;

import com.meratransport.trip.entity.Trip;

public interface TrackingService {

	
	Map<String, Object> getTracking(String tripId, Map<String, String> map) throws Exception;

	void registerDevice(String tripId, Map<String, String> prepareHeaderMap) throws Exception;

	void registerTrip(String tripId, Map<String, String> prepareHeaderMap) throws Exception;
	
	Map<String, Object> sendConsent(Trip trip, Map<String, String> headers) throws Exception;
	
	
}
