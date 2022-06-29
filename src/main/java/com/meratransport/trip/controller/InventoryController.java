package com.meratransport.trip.controller;

import static com.meratransport.trip.constant.ApplicationConstant.APP_TYPE;
import static com.meratransport.trip.constant.ApplicationConstant.EMPLOYEE_ID;
import static com.meratransport.trip.constant.ApplicationConstant.TENANT_ID;
import static com.meratransport.trip.constant.ApplicationConstant.USER_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.meratransport.trip.dto.BarCodeRequest;
import com.meratransport.trip.dto.BarCodeRequestAtSource;
import com.meratransport.trip.dto.GPSDevicesResponse;
import com.meratransport.trip.dto.InventoryRequest;
import com.meratransport.trip.dto.InventoryResponse;
import com.meratransport.trip.dto.UpdateDamageInventory;
import com.meratransport.trip.service.InventoryService;

@RestController
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

	@PostMapping("/addTagsToTrip")
	public boolean addTagsToTrip(@RequestHeader(TENANT_ID) String tenantId,
			@RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId,
			@RequestHeader(APP_TYPE) String appType, @RequestBody BarCodeRequestAtSource obj) throws Exception {
		return inventoryService.addTagsToTrip(obj, prepareHeaderMap(tenantId, userId, employeeId, appType));
	}

	@PostMapping("/scanTripTags")
	public boolean scanTagsToTrip(@RequestHeader(TENANT_ID) String tenantId,
			@RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId,
			@RequestHeader(APP_TYPE) String appType, @RequestBody BarCodeRequest obj) throws Exception {
		return inventoryService.scanTripTags(obj, prepareHeaderMap(tenantId, userId, employeeId, appType));
	}

	@PostMapping("/addDeviceToInventory")
	public InventoryResponse addDeviceToInventory(@RequestHeader(TENANT_ID) String tenantId,
			@RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId,
			@RequestHeader(APP_TYPE) String appType, @RequestBody InventoryRequest obj) throws Exception {
		return inventoryService.addDeviceToInventory(obj, prepareHeaderMap(tenantId, userId, employeeId, appType));
	}

	@GetMapping("/getInventoryDetails")
	public InventoryResponse getDetails(@RequestHeader(TENANT_ID) String tenantId,
			@RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId,
			@RequestHeader(APP_TYPE) String appType, boolean viewDevicesNumbers) throws Exception {
		return inventoryService.getDetails(tenantId, viewDevicesNumbers,
				prepareHeaderMap(tenantId, userId, employeeId, appType));
	}

	@PostMapping("/updateInventory")
	public boolean updateInVentory(@RequestHeader(TENANT_ID) String tenantId,
			@RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId,
			@RequestHeader(APP_TYPE) String appType, @RequestBody UpdateDamageInventory obj,
			boolean isTagInventory) throws Exception {
		return inventoryService.updateInVentory(obj, isTagInventory,
				prepareHeaderMap(tenantId, userId, employeeId, appType));
	}

	@GetMapping("/getDevices")
	public List<GPSDevicesResponse> getDevicesList(@RequestHeader(TENANT_ID) String tenantId,
			@RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId,
			@RequestHeader(APP_TYPE) String appType) throws Exception {
		return inventoryService.getDevicesList(prepareHeaderMap(tenantId, userId, employeeId, appType));

	}

	private Map<String, String> prepareHeaderMap(String tenantId, String userId, String employeeId, String appType) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(TENANT_ID, tenantId);
		headers.put(EMPLOYEE_ID, employeeId);
		headers.put(USER_ID, userId);
		headers.put(APP_TYPE, appType);
		return headers;

	}

}
