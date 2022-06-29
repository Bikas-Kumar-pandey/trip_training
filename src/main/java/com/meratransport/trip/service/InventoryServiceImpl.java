package com.meratransport.trip.service;

import static com.meratransport.trip.constant.ApplicationConstant.ADD_BARCODE;
import static com.meratransport.trip.constant.ApplicationConstant.DAMAGED;
import static com.meratransport.trip.constant.ApplicationConstant.DROP;
import static com.meratransport.trip.constant.ApplicationConstant.EMPLOYEE_ID;
import static com.meratransport.trip.constant.ApplicationConstant.END_TRIP;
import static com.meratransport.trip.constant.ApplicationConstant.FREE;
import static com.meratransport.trip.constant.ApplicationConstant.GPS;
import static com.meratransport.trip.constant.ApplicationConstant.IN_USE;
import static com.meratransport.trip.constant.ApplicationConstant.MATERIAL;
import static com.meratransport.trip.constant.ApplicationConstant.NO_SHIPPER_EXISTS;
import static com.meratransport.trip.constant.ApplicationConstant.PICKUP;
import static com.meratransport.trip.constant.ApplicationConstant.START_TRIP;
import static com.meratransport.trip.constant.ApplicationConstant.TAG;
import static com.meratransport.trip.constant.ApplicationConstant.TAGS_COUNT_MISMATCH;
import static com.meratransport.trip.constant.ApplicationConstant.TENANT_ID;
import static com.meratransport.trip.constant.ApplicationConstant.TENANT_WITH_ID_EXISTS;
import static com.meratransport.trip.constant.ApplicationConstant.TRIP_NOT_EXISTS;
import static com.meratransport.trip.constant.ApplicationConstant.UNASSIGNED_TAGS;
import static com.meratransport.trip.constant.ApplicationConstant.VERIFY_BARCODE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meratransport.trip.constant.ApplicationConstant;
import com.meratransport.trip.dto.BarCodeRequest;
import com.meratransport.trip.dto.BarCodeRequestAtSource;
import com.meratransport.trip.dto.DamagedTagsInfo;
import com.meratransport.trip.dto.GPSDevicesResponse;
import com.meratransport.trip.dto.InventoryRequest;
import com.meratransport.trip.dto.InventoryResponse;
import com.meratransport.trip.dto.TagInfo;
import com.meratransport.trip.dto.TagInfoAtSource;
import com.meratransport.trip.dto.UpdateDamageInventory;
import com.meratransport.trip.entity.DeviceInfo;
import com.meratransport.trip.entity.DeviceLog;
import com.meratransport.trip.entity.Images;
import com.meratransport.trip.entity.Trip;
import com.meratransport.trip.entity.TripDevices;
import com.meratransport.trip.profile.repository.ShipperRepository;
import com.meratransport.trip.profile.repository.TenantRepository;
import com.meratransport.trip.repository.DeviceInfoRepository;
import com.meratransport.trip.repository.ImagesRepository;
import com.meratransport.trip.repository.InventoryRepository;
import com.meratransport.trip.repository.TripRepository;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private TripRepository repository;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private DeviceInfoRepository deviceInfoRepository;

	@Autowired
	private ImagesRepository imagesRepository;

	@Autowired
	private ShipperRepository shipperRepository;

	@Autowired
	private TenantRepository tenantRepository;

	@Override
	@Transactional
	public boolean addTagsToTrip(BarCodeRequestAtSource obj, Map<String, String> headers) throws Exception {
		isValidRequest(headers);

		if (obj != null) {
			Optional<Trip> tripObj = repository.findById(obj.getTripId());

			if (!tripObj.isPresent()) {
				throw new Exception(TRIP_NOT_EXISTS);
			}
			Trip trip = tripObj.get();

			if (obj.getTagInfo() != null) {

				if (trip.getTagCount() != obj.getTagInfo().size()) {
					throw new Exception("Tag Count Mismatch ! Assigned Tags to this trip " + trip.getTagCount());
				}

				DeviceInfo deviceInfo = deviceInfoRepository.findByTenantId(headers.get(TENANT_ID));
				if (deviceInfo == null) {
					throw new Exception("Register Tenant First");
				}

				List<DeviceLog> deviceInfos = new ArrayList<DeviceLog>();
				List<TagInfoAtSource> tagsInfos = obj.getTagInfo();

				// TagNumber,Object
				Map<String, TagInfoAtSource> tagsWithDirection = validateTags(tagsInfos);

				List<DeviceLog> deviceLogs = inventoryRepository.findByTripIdAndTypeAndStatus(trip.getId(), TAG,
						IN_USE);
				if (deviceLogs != null && !deviceLogs.isEmpty()) {
					deviceLogs.stream().forEach(i -> {
						i.setStatus(FREE);
						i.setDirection(null);
					});
					inventoryRepository.saveAll(deviceLogs);
				}

				List<String> allTags = tagsInfos.stream().map(TagInfoAtSource::getNumber).collect(Collectors.toList());

				List<DeviceLog> tagsInfo = inventoryRepository.findBySerialNoInAndTypeAndTenantId(allTags, TAG,
						headers.get(TENANT_ID));

				List<String> tagsInDb = tagsInfo.stream().map(DeviceLog::getSerialNo).collect(Collectors.toList());

				allTags.removeAll(tagsInDb);

				allTags.stream().forEach(i -> {
					DeviceLog deviceLog = new DeviceLog();
					deviceLog.setProvider(obj.getProvider());
					deviceLog.setSerialNo(i);
					deviceLog.setTenantId(headers.get(TENANT_ID));
					deviceLog.setStatus(IN_USE);
					deviceLog.setType(TAG);
					deviceLog.setDeviceInfo(deviceInfo);
					deviceLog.setDirection(tagsWithDirection.get(i).getDirection());
					deviceInfos.add(deviceLog);

				});

				tagsInfo.stream().forEach(i -> {
					i.setStatus(IN_USE);
					i.setSource(trip.getLocation().stream()
							.filter(location -> location.getActivity().equalsIgnoreCase(PICKUP)).findFirst().get()
							.getLocationId());

					i.setDestination(trip.getLocation().stream()
							.filter(location -> location.getActivity().equalsIgnoreCase(DROP)).findFirst().get()
							.getLocationId());

					i.setDirection(tagsWithDirection.get(i.getSerialNo()).getDirection());
					deviceInfos.add(i);
				});

				inventoryRepository.saveAll(deviceInfos);

				List<String> deviceIds = new ArrayList<String>();

				deviceInfos.stream().forEach(i -> {
					deviceIds.add(i.getId());
				});

				imagesRepository.deleteByReferenceIdInAndType(deviceIds, ApplicationConstant.TAG);

				deviceInfos.stream().forEach(i -> {
					Images img = new Images();
					img.setImgLink(tagsWithDirection.get(i.getSerialNo()).getImgLink());
					img.setType(TAG);
					img.setReferenceId(i.getId());
					imagesRepository.save(img);
				});

				List<TripDevices> tripDevices = new ArrayList<TripDevices>();

				deviceInfos.stream().forEach(i -> {
					TripDevices devices = new TripDevices();
					devices.setDeviceId(i.getId());
					devices.setDeviceType(TAG);
					devices.setActive(true);
					devices.setStatus(IN_USE);
					tripDevices.add(devices);

				});
				
				List<DeviceLog> gps = inventoryRepository.findByTripIdAndType(obj.getTripId(), GPS);
				deviceInfos.addAll(gps);

				trip.setDeviceInfo(deviceInfos);

				trip.setDevices(tripDevices);
			}

			trip.setCurrentStatus(ApplicationConstant.SCAN_TAGS);
			trip.setNextStatus(ApplicationConstant.ADD_MATERIAL);

			repository.save(trip);

		}

		return true;
	}

	private Map<String, TagInfoAtSource> validateTags(List<TagInfoAtSource> tagsInfos) throws Exception {

		List<String> index = new ArrayList<String>();
		Map<String, List<String>> tags = new HashMap<String, List<String>>();
		Map<String, List<String>> duplicate = new HashMap<String, List<String>>();

		if (tagsInfos != null) {
			for (TagInfoAtSource tagInfoAtSource : tagsInfos) {
				if (tags.containsKey(tagInfoAtSource.getDirection())) {
					List<String> tagsList = tags.get(tagInfoAtSource.getDirection());
					tagsList.add(tagInfoAtSource.getNumber());
					tags.put(tagInfoAtSource.getDirection(), tagsList);
					index.add(tagInfoAtSource.getDirection());
				} else {
					List<String> tagsList = new ArrayList<String>();
					tagsList.add(tagInfoAtSource.getNumber());
					tags.put(tagInfoAtSource.getDirection(), tagsList);
				}
			}
		}

		for (String str : index) {
			duplicate.put(str, tags.get(str));
		}

		if (!index.isEmpty()) {
			throw new Exception("Duplicate Tags found for the same positions :  " + duplicate);
		}

		Map<String, TagInfoAtSource> result = tagsInfos.stream()
				.collect(Collectors.toMap(TagInfoAtSource::getNumber, Function.identity()));

		return result;
	}

	@Override
	@Transactional
	public boolean scanTripTags(BarCodeRequest obj, Map<String, String> headers) throws Exception {
		isValidRequest(headers);

		if (obj != null) {

			Optional<Trip> tripObj = repository.findById(obj.getTripId());

			if (!tripObj.isPresent()) {
				throw new Exception(TRIP_NOT_EXISTS);
			}

			Trip trip = tripObj.get();

			List<DeviceLog> tags = inventoryRepository.findByTripIdAndType(obj.getTripId(), TAG);
			List<TagInfo> tagList = obj.getTagNumber();

			if (tags.size() != tagList.size()) {
				throw new Exception(TAGS_COUNT_MISMATCH);
			}

			Map<String, DeviceLog> mapOfTags = tags.stream()
					.collect(Collectors.toMap(DeviceLog::getSerialNo, Function.identity()));

			List<String> deviceIds = tags.stream().map(i -> i.getId()).collect(Collectors.toList());

			List<Images> images = imagesRepository.findByReferenceIdInAndType(deviceIds, TAG);
			

			Map<String, Images> mapOfImages = images.stream()
					.collect(Collectors.toMap(Images::getReferenceId, Function.identity()));

			List<DeviceLog> updatedTagStatus = new ArrayList<DeviceLog>();
			List<Images> updatedImages = new ArrayList<Images>();

			for (TagInfo tag : tagList) {
				DeviceLog device = mapOfTags.get(tag.getNumber());
				device.setStatus(tag.getStatus());
				updatedTagStatus.add(device);
				Images img = mapOfImages.get(device.getId());
				img.setImgLinkDestination(tag.getImgLink());
				updatedImages.add(img);

			}
			
			List<DeviceLog> gps = inventoryRepository.findByTripIdAndType(obj.getTripId(), GPS);
			updatedTagStatus.addAll(gps);

			inventoryRepository.saveAll(updatedTagStatus);
			imagesRepository.saveAll(updatedImages);

			trip.setCurrentStatus(VERIFY_BARCODE);
			trip.setNextStatus(END_TRIP);
			repository.save(trip);

		}

		return true;
	}

	@Override
	@Transactional
	public InventoryResponse addDeviceToInventory(InventoryRequest obj, Map<String, String> headers) throws Exception {
		isValidRequest(headers);
		DeviceInfo deviceInfo = null;
		List<String> gpsDevices = obj.getGpsDeviceNumber();
		List<String> tags = obj.getTagsNumber();
		List<String> existingTags = new ArrayList<String>();
		List<String> existingGps = new ArrayList<String>();
		List<DeviceLog> gpsLogs = null;
		List<DeviceLog> tagsLog = null;
		List<DeviceLog> list = null;

		if (deviceInfoRepository.countByTenantId(headers.get(TENANT_ID)) > 0) {
			deviceInfo = deviceInfoRepository.findByTenantId(headers.get(TENANT_ID));
			list = inventoryRepository.findByTenantId(headers.get(TENANT_ID));

			list.stream().forEach(inventory -> {
				if (inventory.getType().equalsIgnoreCase(GPS)) {
					existingGps.add(inventory.getSerialNo());
				} else {
					existingTags.add(inventory.getSerialNo());
				}
			});

		} else {
			deviceInfo = new DeviceInfo();
			deviceInfo.setContractEndDate(obj.getContractEndDate());
			deviceInfo.setContractStartDate(obj.getContractStartDate());
			deviceInfo.setPrice(obj.getPrice());
			deviceInfo.setProvider(obj.getProvider());
			deviceInfo.setTenantId(headers.get(TENANT_ID));
		}

		if (gpsDevices != null) {
			gpsLogs = new ArrayList<DeviceLog>();
			for (String deviceNumber : gpsDevices) {
				if (!existingGps.contains(deviceNumber)) {
					DeviceLog deviceLog = new DeviceLog();
					deviceLog.setProvider(obj.getProvider());
					deviceLog.setSerialNo(deviceNumber);
					deviceLog.setTenantId(headers.get(TENANT_ID));
					deviceLog.setStatus(FREE);
					deviceLog.setType(GPS);
					deviceLog.setDeviceInfo(deviceInfo);
					gpsLogs.add(deviceLog);
				}
			}

			inventoryRepository.saveAll(gpsLogs);

		}

		if (tags != null) {
			tagsLog = new ArrayList<DeviceLog>();

			for (String tagNumber : tags) {
				if (!existingTags.contains(tagNumber)) {
					DeviceLog deviceLog = new DeviceLog();
					deviceLog.setProvider(obj.getProvider());
					deviceLog.setSerialNo(tagNumber);
					deviceLog.setTenantId(headers.get(TENANT_ID));
					deviceLog.setStatus(FREE);
					deviceLog.setType(TAG);
					deviceLog.setDeviceInfo(deviceInfo);
					tagsLog.add(deviceLog);
				}
			}

			inventoryRepository.saveAll(tagsLog);
		}

		List<DeviceLog> allTags = new ArrayList<DeviceLog>();
		allTags.addAll(tagsLog);
		allTags.addAll(gpsLogs);
		if (list != null) {
			list.removeAll(allTags);
			allTags.addAll(list);

		}

		deviceInfo.setDevices(allTags);
		deviceInfoRepository.save(deviceInfo);

		return getDetails(deviceInfo.getTenantId(), true, headers);
	}

	@Override
	public InventoryResponse getDetails(String tenantId, boolean viewDevicesNumbers, Map<String, String> headers)
			throws Exception {
		isValidRequest(headers);

		InventoryResponse inventoryResponse = new InventoryResponse();

		DeviceInfo deviceInfo = deviceInfoRepository.findByTenantId(tenantId);

		if (deviceInfo == null) {
			throw new Exception("No Inventory Info Present for the given tenant");
		}
		List<String> gps = new ArrayList<String>();
		List<String> tags = new ArrayList<String>();

		inventoryResponse.setContractEndDate(deviceInfo.getContractEndDate());
		inventoryResponse.setContractStartDate(deviceInfo.getContractStartDate());
		inventoryResponse.setPrice(deviceInfo.getPrice());
		inventoryResponse.setProvider(deviceInfo.getProvider());
		inventoryResponse.setId(deviceInfo.getId());

		deviceInfo.getDevices().stream().forEach(obj -> {
			if (obj.getType().equalsIgnoreCase(GPS)) {
				gps.add(obj.getSerialNo());
			} else {
				tags.add(obj.getSerialNo());
			}
		});

		if (viewDevicesNumbers) {
			inventoryResponse.setTagsNumber(tags);
			inventoryResponse.setGpsDeviceNumbder(gps);
		}

		inventoryResponse.setGpsDeviceCount(gps.size());
		inventoryResponse.setTagCount(tags.size());

		return inventoryResponse;
	}

	@Override
	@Transactional
	public boolean updateInVentory(UpdateDamageInventory obj, boolean isTagInventory, Map<String, String> headers)
			throws Exception {

		isValidRequest(headers);
		List<String> numbers = obj.getInfo().stream().map(DamagedTagsInfo::getSerialNumbers)
				.collect(Collectors.toList());
		;

		List<Images> imgs = new ArrayList<Images>();

		String type = isTagInventory ? TAG : GPS;

		List<DeviceLog> tagsInfo = inventoryRepository.findBySerialNoInAndType(numbers, type);
		List<DeviceLog> updatedList = new ArrayList<DeviceLog>();

		tagsInfo.stream().forEach(i -> {
			i.setStatus(DAMAGED);
			updatedList.add(i);

		});

		inventoryRepository.saveAll(updatedList);

		int index = 0;

		for (DamagedTagsInfo info : obj.getInfo()) {

			for (String i : info.getImgs()) {
				Images img = new Images();
				img.setDamaged(true);
				img.setImgLink(i);
				img.setReason(obj.getReason());
				img.setNotes(obj.getNotes());
				img.setType(type);
				img.setReferenceId(tagsInfo.get(index).getId());
				imgs.add(img);

			}

			index++;

		}

		imagesRepository.saveAll(imgs);

		return true;
	}

	@Override
	public List<GPSDevicesResponse> getDevicesList(Map<String, String> headers) throws Exception {
		isValidRequest(headers);

		List<DeviceLog> devices = inventoryRepository.findByTenantIdAndType(headers.get(TENANT_ID), GPS);
		List<GPSDevicesResponse> gps = new ArrayList<GPSDevicesResponse>();

		for (DeviceLog deviceLog : devices) {
			GPSDevicesResponse devicesResponse = new GPSDevicesResponse();
			devicesResponse.setGpsNumber(deviceLog.getSerialNo());
			devicesResponse.setStatus(deviceLog.getStatus());
			gps.add(devicesResponse);
		}

		return gps;
	}

	private void isValidRequest(Map<String, String> headers) throws Exception {

		if (!shipperRepository.existsById(headers.get(EMPLOYEE_ID))) {
			throw new Exception(NO_SHIPPER_EXISTS);
		}

		if (!tenantRepository.existsById(headers.get(TENANT_ID))) {
			throw new Exception(END_TRIP);
		}

	}

}
