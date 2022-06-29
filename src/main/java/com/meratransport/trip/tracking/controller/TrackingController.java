package com.meratransport.trip.tracking.controller;

import static com.meratransport.trip.constant.ApplicationConstant.APP_TYPE;
import static com.meratransport.trip.constant.ApplicationConstant.EMPLOYEE_ID;
import static com.meratransport.trip.constant.ApplicationConstant.TENANT_ID;
import static com.meratransport.trip.constant.ApplicationConstant.USER_ID;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.meratransport.trip.constant.ApplicationConstant;
import com.meratransport.trip.tracking.service.TrackingService;

@RestController
public class TrackingController {

	@Autowired
	public TrackingService trackingService;

	@PostMapping("/trip/track/registerDevice/{tripId}")
	public void registerDevice(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId,
			@RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType,
			@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId,
			@PathVariable String tripId) throws Exception {

		trackingService.registerDevice(tripId, prepareHeaderMap(tenantId, userId, employeeId, appType, groupId));
	}

	@PostMapping("/trip/track/registerTrip/{tripId}")
	public void registerTrip(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId,
			@RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType,
			@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId,
			@PathVariable String tripId) throws Exception {

		trackingService.registerTrip(tripId, prepareHeaderMap(tenantId, userId, employeeId, appType, groupId));
	}

	@PostMapping("/trip/track/{tripId}")
	public Map<String, Object> getTrackingDetails(@RequestHeader(TENANT_ID) String tenantId,
			@RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId,
			@RequestHeader(APP_TYPE) String appType,
			@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId,
			@PathVariable String tripId) throws Exception {

		return trackingService.getTracking(tripId, prepareHeaderMap(tenantId, userId, employeeId, appType, groupId));
	}

	private Map<String, String> prepareHeaderMap(String tenantId, String userId, String employeeId, String appType,
			String groupId) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(TENANT_ID, tenantId);
		headers.put(EMPLOYEE_ID, employeeId);
		headers.put(USER_ID, userId);
		headers.put(APP_TYPE, appType);
		headers.put(ApplicationConstant.GROUP_ID, groupId);
		return headers;

	}

}
