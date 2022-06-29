package com.meratransport.trip.service;

import java.util.List;
import java.util.Map;

import com.meratransport.trip.dto.BarCodeRequest;
import com.meratransport.trip.dto.BarCodeRequestAtSource;
import com.meratransport.trip.dto.GPSDevicesResponse;
import com.meratransport.trip.dto.InventoryRequest;
import com.meratransport.trip.dto.InventoryResponse;
import com.meratransport.trip.dto.UpdateDamageInventory;

public interface InventoryService {

	boolean addTagsToTrip(BarCodeRequestAtSource obj, Map<String, String> headers) throws Exception;

	boolean scanTripTags(BarCodeRequest obj, Map<String, String> headers) throws Exception;

	InventoryResponse addDeviceToInventory(InventoryRequest obj, Map<String, String> headers) throws Exception;

	InventoryResponse getDetails(String tenantId, boolean viewDevicesNumbers, Map<String, String> headers) throws Exception;

	boolean updateInVentory(UpdateDamageInventory obj, boolean isTagInventory, Map<String, String> headers) throws Exception;

	List<GPSDevicesResponse> getDevicesList(Map<String, String> headers) throws Exception;

}
