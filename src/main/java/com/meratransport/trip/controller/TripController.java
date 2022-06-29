package com.meratransport.trip.controller;

import static com.meratransport.trip.constant.ApplicationConstant.APP_TYPE;
import static com.meratransport.trip.constant.ApplicationConstant.EMPLOYEE_ID;
import static com.meratransport.trip.constant.ApplicationConstant.TENANT_ID;
import static com.meratransport.trip.constant.ApplicationConstant.USER_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meratransport.trip.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meratransport.trip.constant.ApplicationConstant;
import com.meratransport.trip.service.TripService;

@RestController
public class TripController {

	@Autowired
	TripService tripService;

	@GetMapping("/trip/{tripId}")
	public TripResponse getTrip(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId,
			@RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType,
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId,
			@PathVariable String tripId) throws Exception {
		return tripService.getTrip(tripId, prepareHeaderMap(tenantId, userId, employeeId, appType, groupId),
				prepareHttpHeader(tenantId, userId, employeeId, appType, authorization));
	}

	@GetMapping("/getAllTrips")
	public List<TripsResponse> getAllTrip(@RequestHeader(TENANT_ID) String tenantId,
			@RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId,
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestHeader(APP_TYPE) String appType, Pageable pageable,
			@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId, String vehicleNumber)
			throws Exception {
		return tripService.getAllTrip(pageable, prepareHeaderMap(tenantId, userId, employeeId, appType,groupId), vehicleNumber,
				prepareHttpHeader(tenantId, userId, employeeId, appType, authorization));
	}

	@PostMapping("/trip")
	public TripResponse createTrip(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId,
			@RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType,
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId,
			@RequestBody TripRequest request) throws Exception {
		return tripService.createTrip(request, prepareHeaderMap(tenantId, userId, employeeId, appType, groupId),
				prepareHttpHeader(tenantId, userId, employeeId, appType, authorization));
	}

	@PostMapping("/trip/material")
	public TripResponse addMaterial(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId,
			@RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType,
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId,
			@RequestBody MaterialRequest materialRequest) throws Exception {
		return tripService.addMaterialInfo(materialRequest,
				prepareHeaderMap(tenantId, userId, employeeId, appType, groupId),
				prepareHttpHeader(tenantId, userId, employeeId, appType, authorization));

	}

	@PostMapping("/trip/changeTripStatus")
	public boolean changeTripStatus(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId,
			@RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType,
			@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId,
		    @RequestBody StatusUpdate statusUpdate)
			throws Exception {
		return tripService.changeTripStatus(statusUpdate,
				prepareHeaderMap(tenantId, userId, employeeId, appType, groupId));

	}

	@PostMapping("trip/addInventoryToTrip")
	public boolean addInventoryToTrip(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId,
			@RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType,
			@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId,
			@RequestBody TripInventory inventory) throws Exception {
		return tripService.addInventoryToTrip(inventory,
				prepareHeaderMap(tenantId, userId, employeeId, appType, groupId));

	}

	@PutMapping("/updateTrip/{tripId}")
	public TripResponse updateTrip(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId,
			@RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType,
			@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable String tripId,
			@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId,
			@RequestBody TripRequest request) throws Exception {
		return tripService.updateTrip(tripId, request, prepareHeaderMap(tenantId, userId, employeeId, appType, groupId),
				prepareHttpHeader(tenantId, userId, employeeId, appType, authorization));
	}

	@GetMapping("/trip/status/{tripId}")
	public TripResponse getTripStatus(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId,
			@RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType,
			@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId,
			@PathVariable String tripId) throws Exception {
		return tripService.getTripStatus(tripId, prepareHeaderMap(tenantId, userId, employeeId, appType, groupId));
	}

	@DeleteMapping("/deleteTrip/{tripId}")
	public boolean deleteTrip(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId,
			@RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType,
			@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId,
			@PathVariable String tripId) throws Exception {
		return tripService.deleteTrip(tripId, prepareHeaderMap(tenantId, userId, employeeId, appType, groupId));
	}

	@PostMapping("/addDriver/{tripId}")
	public boolean addDriver(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId,
							 @RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType, @RequestHeader(value = "Authorization", required = false)
							 String authorization,@RequestHeader(value = ApplicationConstant.GROUP_ID, required = false) String groupId, @RequestBody DriverRequest driverRequest, @PathVariable String tripId) throws Exception {
		return tripService.addDriver(tripId, driverRequest,  prepareHeaderMap(tenantId, userId, employeeId, appType,groupId));
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

	private HttpHeaders prepareHttpHeader(String tenantId, String userId, String employeeId, String appType,
			String authorization) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(TENANT_ID, tenantId);
		headers.set(EMPLOYEE_ID, employeeId);
		headers.set(USER_ID, userId);
		headers.set(APP_TYPE, appType);
		headers.set("Authorization", authorization);
		return headers;
	}
}
