package com.meratransport.trip.service;

import static com.meratransport.trip.constant.ApplicationConstant.APP_TYPE;
import static com.meratransport.trip.constant.ApplicationConstant.DRIVER;
import static com.meratransport.trip.constant.ApplicationConstant.DROP;
import static com.meratransport.trip.constant.ApplicationConstant.EMPLOYEE_ID;
import static com.meratransport.trip.constant.ApplicationConstant.END_TRIP;
import static com.meratransport.trip.constant.ApplicationConstant.GPS;
import static com.meratransport.trip.constant.ApplicationConstant.IN_USE;
import static com.meratransport.trip.constant.ApplicationConstant.MASTER_DRIVER_NOT_FOUND;
import static com.meratransport.trip.constant.ApplicationConstant.MASTER_VEHICLE_NOT_FOUND;
import static com.meratransport.trip.constant.ApplicationConstant.MATERIAL;
import static com.meratransport.trip.constant.ApplicationConstant.PICKUP;
import static com.meratransport.trip.constant.ApplicationConstant.SCAN_TAGS;
import static com.meratransport.trip.constant.ApplicationConstant.SCAN_TAGS_UNLOAD;
import static com.meratransport.trip.constant.ApplicationConstant.START_INSTALLATION;
import static com.meratransport.trip.constant.ApplicationConstant.START_TRIP;
import static com.meratransport.trip.constant.ApplicationConstant.TENANT_ID;
import static com.meratransport.trip.constant.ApplicationConstant.TRIP_ALREADY_ENDED;
import static com.meratransport.trip.constant.ApplicationConstant.TRIP_COMPLETED;
import static com.meratransport.trip.constant.ApplicationConstant.TRIP_CREATED;
import static com.meratransport.trip.constant.ApplicationConstant.TRIP_NOT_EXISTS;
import static com.meratransport.trip.constant.ApplicationConstant.VEHICLE;
import static com.meratransport.trip.constant.ApplicationConstant.VERIFY_BARCODE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meratransport.trip.constant.ApplicationConstant;
import com.meratransport.trip.constant.TripType;
import com.meratransport.trip.dto.ConsignmentRequest;
import com.meratransport.trip.dto.ConsignmentResponse;
import com.meratransport.trip.dto.DamagedItems;
import com.meratransport.trip.dto.Driver;
import com.meratransport.trip.dto.DriverRequest;
import com.meratransport.trip.dto.DriverResponse;
import com.meratransport.trip.dto.Location;
import com.meratransport.trip.dto.LocationRequest;
import com.meratransport.trip.dto.LocationResponse;
import com.meratransport.trip.dto.MaterialRequest;
import com.meratransport.trip.dto.StatusUpdate;
import com.meratransport.trip.dto.TagInfoResponse;
import com.meratransport.trip.dto.TripInventory;
import com.meratransport.trip.dto.TripRequest;
import com.meratransport.trip.dto.TripResponse;
import com.meratransport.trip.dto.TripsResponse;
import com.meratransport.trip.dto.Vehicle;
import com.meratransport.trip.dto.VehicleRequest;
import com.meratransport.trip.dto.VehicleResponse;
import com.meratransport.trip.entity.DeviceInfo;
import com.meratransport.trip.entity.DeviceLog;
import com.meratransport.trip.entity.Images;
import com.meratransport.trip.entity.Trip;
import com.meratransport.trip.entity.TripConsignment;
import com.meratransport.trip.entity.TripDriver;
import com.meratransport.trip.entity.TripLocation;
import com.meratransport.trip.entity.TripSequence;
import com.meratransport.trip.entity.TripVehicles;
import com.meratransport.trip.location.repository.LocationMasterRepository;
import com.meratransport.trip.picklist.entity.CommonPickList;
import com.meratransport.trip.picklist.repository.PickListRepository;
import com.meratransport.trip.profile.entity.DriverMaster;
import com.meratransport.trip.profile.entity.VehicleMaster;
import com.meratransport.trip.profile.entity.VehicleNumberInfo;
import com.meratransport.trip.profile.entity.Vendor;
import com.meratransport.trip.profile.repository.ProfileDriverMasterRepository;
import com.meratransport.trip.profile.repository.ShipperRepository;
import com.meratransport.trip.profile.repository.TenantRepository;
import com.meratransport.trip.profile.repository.VehicleMasterRepository;
import com.meratransport.trip.repository.DeviceInfoRepository;
import com.meratransport.trip.repository.ImagesRepository;
import com.meratransport.trip.repository.InventoryRepository;
import com.meratransport.trip.repository.TripRepository;
import com.meratransport.trip.tracking.service.TrackingService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TripServiceImpl implements TripService {

	@Autowired
	private TripRepository repository;

	@Autowired
	private ProfileDriverMasterRepository driverMasterRepository;

	@Autowired
	private VehicleMasterRepository vehicleMasterRepository;

	@Autowired
	private ImagesRepository imagesRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private ShipperRepository shipperRepository;

	@Autowired
	private TenantRepository tenantRepository;

	@Autowired
	private PickListRepository pickListRepository;

	@Autowired
	private DeviceInfoRepository deviceInfoRepository;

	@Autowired
	private TrackingService trackingService;

	@Value("${base.location.uri}")
	private String locationUri;

	@Override
	@Transactional
	public TripResponse createTrip(TripRequest request, Map<String, String> headers, HttpHeaders httpHeaders)
			throws Exception {

		isValidRequest(headers);

		Trip trip = new Trip();

		trip.setAppType(headers.get(APP_TYPE));
		trip.setTenantId(headers.get(TENANT_ID));
		trip.setEmployeeId(headers.get(EMPLOYEE_ID));
		setLocationForTrip(trip, request, headers.get(TENANT_ID), httpHeaders);

		setDriver(trip, request);

		setVehicle(trip, request);

		setConsignment(trip, request);

		trip.setTripType(TripType.valueOf("FTL"));
		trip.setCurrentStatus(TRIP_CREATED);
		trip.setNextStatus(START_INSTALLATION);

		trip.setTransporterName(request.getTransporterName());

		trip.setSequence(new TripSequence());

		trip = repository.save(trip);

		setImages(trip.getDriver().get(0).getId(), request.getDriver().getImgs(), DRIVER);

		setImages(trip.getVehicles().get(0).getId(), request.getVehicle().getImgs(), VEHICLE);

		return getTrip(trip.getId(), headers, httpHeaders);
	}

	private void setImages(String refernceId, List<String> list, String type) {
		if (list != null) {
			for (String img : list) {
				Images images = new Images();
				images.setImgLink(img);
				images.setType(type);
				images.setReferenceId(refernceId);
				imagesRepository.save(images);
			}
		}

	}



	@Override

	public boolean addDriver(String tripId, DriverRequest driverRequest, Map<String, String> headers) throws Exception{
		isValidRequest(headers);

		Optional<Trip> tripObj = repository.findById(tripId);

		if (tripObj.isEmpty()) {
			throw new Exception(TRIP_NOT_EXISTS);
		}

		Trip trip = tripObj.get();

		TripResponse response = new TripResponse();
		List<DriverRequest> drivers = new ArrayList<>();
		drivers.add(driverRequest);
		List<TripDriver> driverList =trip.getDriver();
		for(DriverRequest driver:drivers) {
			TripDriver tripDriver = new TripDriver();
			if (driver != null) {
				Optional<DriverMaster> driverMasterObj = driverMasterRepository.findById(driver.getId());
				DriverMaster driverMaster = driverMasterObj.get();

				if (driverMaster == null) {
					driverMaster = new DriverMaster();
					driverMaster.setActive(true);
					driverMaster.setEmailId(driver.getEmailId());
					driverMaster.setLanguage(driver.getLanguage());
					driverMaster.setMobileNumber(driver.getMobileNumber());
					driverMaster.setName(driver.getName());
					driverMaster.setOwnershipType(driver.getOwnershipType());
					driverMaster.setShortName(driver.getShortName());
					driverMaster.setTitle(driver.getTitle());
					driverMaster.setId(UUID.randomUUID().toString().replaceAll("-", ""));
					driverMasterRepository.save(driverMaster);
				}
				tripDriver.setDriverId(driverMaster.getId());
				tripDriver.setActive(driver.isActive());
			}
			driverList.add(tripDriver);
		}
		trip.setDriver(driverList);

		 repository.save(trip);
		return true;
	}

	private void setConsignment(Trip trip, TripRequest request) {
		TripConsignment tripConsignment = new TripConsignment();

		ConsignmentRequest consignment = request.getConsignment();

		if (consignment != null) {
			tripConsignment.setWeight(consignment.getWeight());
			tripConsignment.setGoodsType(consignment.getGoodsType());
			tripConsignment.setSku(consignment.getSku());
			tripConsignment.setDescription(consignment.getDescription());
			tripConsignment.setVolume(consignment.getVolume());
		}

		List<TripConsignment> consignmentList = new ArrayList<TripConsignment>();
		consignmentList.add(tripConsignment);

		trip.setConsignment(consignmentList);
	}

	private void setVehicle(Trip trip, TripRequest request) {

		TripVehicles tripVehicles = new TripVehicles();
		VehicleRequest vehicle = request.getVehicle();
		if (vehicle != null) {
			VehicleNumberInfo vehicleMaster = vehicleMasterRepository.findById(vehicle.getId()).get();

			if (vehicleMaster == null) {
				vehicleMaster = new VehicleNumberInfo();
				VehicleMaster master = new VehicleMaster();
				Vendor vendor = new Vendor();
				vendor.setName(vehicle.getVendorName());
				vehicleMaster.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				// Needs to be changed Later via API
				vehicleMaster.setVehicleNumber(vehicle.getVehicleNumber());
				Random rnd = new Random();
				int val = rnd.nextInt(9999);
				CommonPickList obj = new CommonPickList(String.valueOf(val),
						UUID.randomUUID().toString().replaceAll("-", ""), vehicle.getVehicleType(),
						ApplicationConstant.VEHICLE_TYPE);
				pickListRepository.save(obj);

				master.setVehicleType(obj.getCommonKey());
				master.setOwnershipType(vehicle.getOwnershipType());
				master.setVendor(vendor);
				vehicleMaster.setMaster(master);
				vehicleMasterRepository.save(vehicleMaster);
			}
			tripVehicles.setVehicleId(vehicleMaster.getId());
			tripVehicles.setActive(true);
			tripVehicles.setVehicleNumber(vehicleMaster.getVehicleNumber());
		}
		List<TripVehicles> vehicles = new ArrayList<TripVehicles>();
		vehicles.add(tripVehicles);
		trip.setVehicles(vehicles);
	}

	private void setDriver(Trip trip, TripRequest request) {

		TripDriver tripDriver = new TripDriver();
		DriverRequest driver = request.getDriver();
		if (driver != null) {
			Optional<DriverMaster> driverMasterObj = driverMasterRepository.findById(driver.getId());
			DriverMaster driverMaster = driverMasterObj.get();

			if (driverMaster == null) {
				driverMaster = new DriverMaster();
				driverMaster.setActive(true);
				driverMaster.setEmailId(driver.getEmailId());
				driverMaster.setLanguage(driver.getLanguage());
				driverMaster.setMobileNumber(driver.getMobileNumber());
				driverMaster.setName(driver.getName());
				driverMaster.setOwnershipType(driver.getOwnershipType());
				driverMaster.setShortName(driver.getShortName());
				driverMaster.setTitle(driver.getTitle());
				driverMaster.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				driverMasterRepository.save(driverMaster);
			}

			tripDriver.setDriverId(driverMaster.getId());
			tripDriver.setActive(true);

		}

		List<TripDriver> drivers = new ArrayList<TripDriver>();
		drivers.add(tripDriver);
		trip.setDriver(drivers);
	}

	private void setLocationForTrip(Trip trip, TripRequest request, String tenantId, HttpHeaders httpHeaders)
			throws Exception {
		TripLocation pickUpTripLocation = new TripLocation();
		TripLocation dropTripLocation = new TripLocation();
		LocationRequest pickUpLocation = request.getPickUpLocation();
		LocationRequest dropLocation = request.getDropLocation();
		JsonObject jsonObject = null;
		ResponseEntity<String> response = null;
		if (pickUpLocation != null) {
			HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);
			RestTemplate restTemplate = new RestTemplate();
			response = restTemplate.exchange(locationUri + pickUpLocation.getLocationId(), HttpMethod.GET,
					requestEntity, String.class);

			jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);
			if (jsonObject.has("response")) {
				jsonObject = (JsonObject) jsonObject.get("response");

				pickUpLocation = new ObjectMapper().findAndRegisterModules().readValue(jsonObject.toString(),
						LocationRequest.class);
			} else {
				pickUpLocation.setActive(true);
				HttpEntity<LocationRequest> post = new HttpEntity<>(pickUpLocation, httpHeaders);
				response = restTemplate.postForEntity(locationUri, post, String.class);
				jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);

				if (jsonObject.has("response")) {
					jsonObject = (JsonObject) jsonObject.get("response");
					pickUpLocation = new ObjectMapper().findAndRegisterModules().readValue(jsonObject.toString(),
							LocationRequest.class);
				} else {
					throw new Exception(response.getBody());
				}

			}
			pickUpTripLocation.setLocationId(pickUpLocation.getLocationId());
			pickUpTripLocation.setActivity(PICKUP);

		}

		if (dropLocation != null) {
			HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);
			RestTemplate restTemplate = new RestTemplate();
			response = restTemplate.exchange(locationUri + dropLocation.getLocationId(), HttpMethod.GET, requestEntity,
					String.class);

			jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);
			if (jsonObject.has("response")) {
				jsonObject = (JsonObject) jsonObject.get("response");

				dropLocation = new ObjectMapper().findAndRegisterModules().readValue(jsonObject.toString(),
						LocationRequest.class);
			} else {
				dropLocation.setActive(true);
				HttpEntity<LocationRequest> post = new HttpEntity<>(dropLocation, httpHeaders);
				response = restTemplate.postForEntity(locationUri, post, String.class);
				jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);

				if (jsonObject.has("response")) {
					jsonObject = (JsonObject) jsonObject.get("response");
					dropLocation = new ObjectMapper().findAndRegisterModules().readValue(jsonObject.toString(),
							LocationRequest.class);
				} else {
					throw new Exception(response.getBody());
				}

			}

			dropTripLocation.setLocationId(dropLocation.getLocationId());
			dropTripLocation.setActivity(DROP);

		}

		List<TripLocation> set = new ArrayList<TripLocation>();
		set.add(pickUpTripLocation);
		set.add(dropTripLocation);
		trip.setLocation(set);

	}

	@Override
	public TripResponse getTrip(String tripId, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception {

		isValidRequest(headers);

		Optional<Trip> tripObj = repository.findById(tripId);

		if (tripObj.isEmpty()) {
			throw new Exception(TRIP_NOT_EXISTS);
		}

		Trip trip = tripObj.get();

		TripResponse response = new TripResponse();

		if (trip.getEmployeeId() != null) {
			response.setShipperName(shipperRepository.findById(trip.getEmployeeId()).get().getName());
		}

		response.setTransporterName(trip.getTransporterName());
		response.setTagCount(trip.getTagCount());
		response.setId(tripId);
		response.setTripType(trip.getTripType());
		response.setCurrentStatus(trip.getCurrentStatus());
		response.setNextStatus(trip.getNextStatus());
		response.setTenantId(trip.getTenantId());
		response.setStartDateTime(trip.getStartedAt());
		response.setSequence(trip.getSequence().getId());
		List<TripLocation> tripLocations = trip.getLocation();
		List<LocationResponse> locationResponses = new ArrayList<LocationResponse>();

		for (TripLocation tripLocation : tripLocations) {
			LocationResponse locationResponse = new LocationResponse();
			locationResponse.setId(tripLocation.getId());
			locationResponse.setInTime(tripLocation.getInTime());
			locationResponse.setOutTime(tripLocation.getOutTime());
			locationResponse.setSerialNo(tripLocation.getSerialNo());
			locationResponse.setActivityTime(tripLocation.getActivityTime());
			locationResponse.setServiceTime(tripLocation.getServiceTime());
			locationResponse.setActivity(tripLocation.getActivity());

			HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);
			RestTemplate restTemplate = new RestTemplate();
			JsonObject jsonObject = null;
			ResponseEntity<String> responseLocation = null;
			LocationRequest locationMaster = null;
			responseLocation = restTemplate.exchange(locationUri + tripLocation.getLocationId(), HttpMethod.GET,
					requestEntity, String.class);

			jsonObject = new Gson().fromJson(responseLocation.getBody(), JsonObject.class);
			if (jsonObject.has("response")) {
				jsonObject = (JsonObject) jsonObject.get("response");

				locationMaster = new ObjectMapper().findAndRegisterModules().readValue(jsonObject.toString(),
						LocationRequest.class);
				Location location = new ObjectMapper().findAndRegisterModules()
						.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
						.convertValue(locationMaster, Location.class);

				locationResponse.setLocation(location);
				locationResponses.add(locationResponse);
			}

		}

		response.setLocations(locationResponses);

		List<TripDriver> drivers = trip.getDriver();
		List<DriverResponse> driverResponses = new ArrayList<DriverResponse>();

		for (TripDriver driver : drivers) {
			DriverResponse driverResponse = new DriverResponse();
			driverResponse.setActive(driver.isActive());
			driverResponse.setId(driver.getId());
			driverResponse.setStatus(driver.getStatus());
			driverResponse.setChangeReason(driver.getChangeReason());

			Optional<DriverMaster> driverMasterObj = driverMasterRepository.findById(driver.getDriverId());
			DriverMaster driverMaster = driverMasterObj.get();
			if (driverMaster == null) {
				throw new Exception(MASTER_DRIVER_NOT_FOUND);
			}

			Driver driverRes = new ObjectMapper().findAndRegisterModules()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
					.convertValue(driverMaster, Driver.class);

			List<Images> imgs = imagesRepository.findByReferenceIdAndType(driver.getId(), DRIVER);

			driverRes.setImgs(imgs);

			driverResponse.setDriver(driverRes);

			driverResponses.add(driverResponse);

		}

		response.setDrivers(driverResponses);
		response.setDrivers(driverResponses);

		List<TripConsignment> consignments = trip.getConsignment();
		List<ConsignmentResponse> consignmentResponses = new ArrayList<ConsignmentResponse>();
		for (TripConsignment consignment : consignments) {
			ConsignmentResponse consignmentResponse = new ConsignmentResponse();
			consignmentResponse.setChangeReason(consignment.getChangeReason());
			consignmentResponse.setDescription(consignment.getDescription());
			consignmentResponse.setGoodsType(consignment.getGoodsType());
			consignmentResponse.setId(consignment.getId());
			consignmentResponse.setQuantity(consignment.getQuantity());
			consignmentResponse.setSku(consignment.getSku());
			consignmentResponse.setVolume(consignment.getVolume());
			consignmentResponse.setWeight(consignment.getWeight());
			consignmentResponses.add(consignmentResponse);
		}

		response.setConsignment(consignmentResponses);

		List<TripVehicles> vehicles = trip.getVehicles();
		System.out.println(vehicles.size());
		List<VehicleResponse> vehicleResponses = new ArrayList<VehicleResponse>();

		for (TripVehicles vehicle : vehicles) {
			VehicleResponse vehicleResponse = new VehicleResponse();

			vehicleResponse.setActive(vehicle.isActive());
			vehicleResponse.setChangeReason(vehicle.getChangeReason());
			vehicleResponse.setId(vehicle.getId());
			vehicleResponse.setStatus(vehicle.getAppType());

			Optional<VehicleNumberInfo> vehicleMasterObj = vehicleMasterRepository.findById(vehicle.getVehicleId());
			VehicleNumberInfo vehicleMaster = vehicleMasterObj.get();

			if (vehicleMaster == null) {
				throw new Exception(MASTER_VEHICLE_NOT_FOUND);
			}

			Vehicle vehicleRes = new Vehicle();

			vehicleRes.setVehicleId(vehicleMaster.getId());
			vehicleRes.setOwnershipType(vehicleMaster.getMaster().getOwnershipType());
			vehicleRes.setVehicleNumber(vehicleMaster.getVehicleNumber());
			vehicleRes.setVehicleType(
					pickListRepository.findByCommonKey(vehicleMaster.getMaster().getVehicleType()).getCommonValue());

			if (vehicleMaster.getMaster().getVendor() != null) {
				vehicleRes.setVendorId(vehicleMaster.getMaster().getVendor().getId());
				vehicleRes.setVendorName(vehicleMaster.getMaster().getVendor().getName());
			}
			List<Images> imgs = imagesRepository.findByReferenceIdAndType(vehicle.getId(), VEHICLE);

			vehicleRes.setImgs(imgs);

			vehicleResponse.setVehicle(vehicleRes);

			vehicleResponses.add(vehicleResponse);

		}
		response.setVehicles(vehicleResponses);

		List<Images> materailImgs = imagesRepository.findByReferenceIdAndType(tripId, MATERIAL);
		response.setMaterialImgs(materailImgs);

		List<DeviceLog> deviceLogs = inventoryRepository.findByTripId(tripId);

		if (deviceLogs != null) {
			List<String> ids = new ArrayList<String>();
			deviceLogs.stream().forEach(obj -> {
				ids.add(obj.getId());
			});

			List<Images> tagsImages = imagesRepository.findByReferenceIdInAndType(ids, ApplicationConstant.TAG);
			Map<String, Images> images = tagsImages.stream()
					.collect(Collectors.toMap(Images::getReferenceId, Function.identity()));

			List<TagInfoResponse> tags = new ArrayList<TagInfoResponse>();
			deviceLogs.stream().forEach(obj -> {
				if (obj != null) {
					TagInfoResponse info = new TagInfoResponse();
					info.setNumber(obj.getSerialNo());
					info.setStatus(obj.getStatus());
					if (!images.isEmpty() && !obj.getType().equalsIgnoreCase(GPS)) {
						info.setImg(images.get(obj.getId()).getImgLink());
						info.setDestinationImg(images.get(obj.getId()).getImgLinkDestination());
					}
					info.setDirection(obj.getDirection());
					info.setType(obj.getType());

					tags.add(info);
				}
			});
			response.setInventory(tags);
		}

		return response;
	}

	@Override
	@Transactional
	public TripResponse addMaterialInfo(MaterialRequest materialRequest, Map<String, String> headers,
			HttpHeaders httpHeaders) throws Exception {

		isValidRequest(headers);

		boolean isTripExists = repository.existsById(materialRequest.getTripId());

		if (!isTripExists) {
			throw new Exception(TRIP_NOT_EXISTS);
		}
		Optional<Trip> tripObj = repository.findById(materialRequest.getTripId());
		Trip trip = tripObj.get();

		// Deleting old imgLink In cases of updated Images
		imagesRepository.deleteByReferenceIdAndTypeAndIsDamaged(trip.getId(), MATERIAL, false);

		List<String> imgs = materialRequest.getImgs();
		if (imgs != null) {
			for (String img : imgs) {
				Images images = new Images();
				images.setImgLink(img);
				images.setType(MATERIAL);
				images.setReferenceId(materialRequest.getTripId());
				images = imagesRepository.save(images);
			}
		}

		trip.setCurrentStatus(ApplicationConstant.ADD_MATERIAL);
		trip.setNextStatus(START_TRIP);

		TripResponse response = getTrip(materialRequest.getTripId(), headers, httpHeaders);

		return response;
	}

	@Override
	@Transactional
	public boolean changeTripStatus(StatusUpdate statusUpdate, Map<String, String> headers)
			throws Exception {

		isValidRequest(headers);

		boolean isExist = repository.existsById(statusUpdate.getTripId());
		if (!isExist) {
			throw new Exception(TRIP_NOT_EXISTS);
		}

		Optional<Trip> tripObj = repository.findById(statusUpdate.getTripId());
		Trip trip = tripObj.get();

		if (trip.getCurrentStatus().equalsIgnoreCase(END_TRIP)) {
			throw new Exception(TRIP_ALREADY_ENDED);
		}

		if (trip.getNextStatus().equalsIgnoreCase(ApplicationConstant.IN_TRANSIT)) {
			if (statusUpdate.getStatus().equalsIgnoreCase(ApplicationConstant.IN_TRANSIT)) {
				statusUpdate.setStatus(ApplicationConstant.SCAN_TAGS_UNLOAD);
			}

			trip.setNextStatus(ApplicationConstant.SCAN_TAGS_UNLOAD);
		}
		if (!trip.getNextStatus().equalsIgnoreCase(statusUpdate.getStatus())) {
			throw new Exception("Flow Mismatch ! The next step for this will be " + trip.getNextStatus());
		}

		switch (statusUpdate.getStatus()) {
		case START_INSTALLATION:
			trip.setCurrentStatus(START_INSTALLATION);
			trip.setNextStatus(ApplicationConstant.INVENTORY_ADDED);
			break;

		case ApplicationConstant.INVENTORY_ADDED:
			trip.setCurrentStatus(ApplicationConstant.INVENTORY_ADDED);
			trip.setNextStatus(SCAN_TAGS);
			break;
		case SCAN_TAGS:
			trip.setCurrentStatus(SCAN_TAGS);
			trip.setNextStatus(ApplicationConstant.ADD_MATERIAL);
			break;
		case ApplicationConstant.ADD_MATERIAL:
			trip.setCurrentStatus(ApplicationConstant.ADD_MATERIAL);
			trip.setNextStatus(ApplicationConstant.START_TRIP);
			break;
		case START_TRIP:
			trip.setCurrentStatus(START_TRIP);
			trip.setNextStatus(ApplicationConstant.IN_TRANSIT);
			trip.setStartedAt(LocalDateTime.now());
			try {

				trackingService.registerDevice(statusUpdate.getTripId(), headers);

			} catch (Exception e) {
				log.info("Exception Occured while registering device " + e.getMessage());
			}

			try {

				trackingService.registerTrip(statusUpdate.getTripId(), headers);

			} catch (Exception e) {
				log.info("Exception Occured while registering Trip " + e.getMessage());
			}

			try {
				 Map<String,Object> consnt =  trackingService.sendConsent(trip, headers);
				 log.info("Consent " + consnt);
				 
			} catch (Exception e) {
				log.info("Exception Occured while registering Trip " + e.getMessage());
			}

			break;

		case ApplicationConstant.IN_TRANSIT:
		case SCAN_TAGS_UNLOAD:
			trip.setCurrentStatus(SCAN_TAGS_UNLOAD);
			trip.setNextStatus(VERIFY_BARCODE);
			break;

		case VERIFY_BARCODE:
			trip.setCurrentStatus(VERIFY_BARCODE);
			trip.setNextStatus(END_TRIP);
			break;
		case END_TRIP:
			trip.setCurrentStatus(END_TRIP);
			trip.setNextStatus(TRIP_COMPLETED);

			List<DamagedItems> damagedItems = statusUpdate.getDamageImgs();

			if (damagedItems != null) {
				for (DamagedItems items : damagedItems) {
					List<String> imgs = items.getImgs();
					for (String imgLink : imgs) {
						Images img = new Images();
						img.setDamaged(true);
						img.setReason(items.getReason());
						img.setNotes(items.getNotes());
						img.setImgLink(imgLink);
						img.setType(MATERIAL);
						img.setReferenceId(statusUpdate.getTripId());
						imagesRepository.save(img);
					}
				}

			}
			break;
		default:
			break;
		}

		repository.save(trip);

		return true;
	}

	@Override
	public List<TripsResponse> getAllTrip(Pageable pageable, Map<String, String> headers, String vehicleNumber,
			HttpHeaders httpHeaders) throws Exception {

		isValidRequest(headers);

		List<Trip> allTrips = null;
		if (vehicleNumber != null) {

			allTrips = repository.findAllByTenantIdAndVehiclesVehicleNumberLike(headers.get(TENANT_ID),
					"%" + vehicleNumber + "%", pageable);

		} else {
			allTrips = repository.findAllByTenantId(headers.get(TENANT_ID), pageable);
		}

		List<TripsResponse> responses = new ArrayList<TripsResponse>();

		for (Trip trip : allTrips) {

			TripsResponse response = new TripsResponse();

			response.setTransporterName(trip.getTransporterName());
			response.setSequence(trip.getSequence().getId());
			HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);
			RestTemplate restTemplate = new RestTemplate();
			JsonObject jsonObject = null;
			ResponseEntity<String> responseLocation = null;
			LocationRequest locationInfo = null;
			responseLocation = restTemplate.exchange(locationUri
					+ trip.getLocation().stream().filter(location -> location.getActivity().equalsIgnoreCase(DROP))
							.findFirst().get().getLocationId(),
					HttpMethod.GET, requestEntity, String.class);

			jsonObject = new Gson().fromJson(responseLocation.getBody(), JsonObject.class);
			if (jsonObject.has("response")) {
				jsonObject = (JsonObject) jsonObject.get("response");

				locationInfo = new ObjectMapper().findAndRegisterModules().readValue(jsonObject.toString(),
						LocationRequest.class);
				response.setDropLocation(locationInfo.getAddress());
			}

			responseLocation = restTemplate.exchange(locationUri
					+ trip.getLocation().stream().filter(location -> location.getActivity().equalsIgnoreCase(PICKUP))
							.findFirst().get().getLocationId(),
					HttpMethod.GET, requestEntity, String.class);

			jsonObject = new Gson().fromJson(responseLocation.getBody(), JsonObject.class);
			if (jsonObject.has("response")) {
				jsonObject = (JsonObject) jsonObject.get("response");

				locationInfo = new ObjectMapper().findAndRegisterModules().readValue(jsonObject.toString(),
						LocationRequest.class);

				response.setPickUplocation(locationInfo.getAddress());
			}

			response.setTripId(trip.getId());
			response.setTripStatus(trip.getCurrentStatus());

			// needs to be change with Vehicle Number
			Optional<VehicleNumberInfo> vehicleMaster = vehicleMasterRepository.findById(
					trip.getVehicles().stream().filter(vehicle -> vehicle.isActive()).findFirst().get().getVehicleId());

			if (vehicleMaster.isPresent()) {

				VehicleNumberInfo vehicle = vehicleMaster.get();

				response.setVehicleNumber(vehicle.getVehicleNumber());
				response.setVehicleId(vehicle.getId());
				response.setVehicleType(
						pickListRepository.findByCommonKey(vehicle.getMaster().getVehicleType()).getCommonValue());

			}

			// needs to be change with Vehicle Number
			Optional<DriverMaster> driverMaster = driverMasterRepository.findById(
					trip.getDriver().stream().filter(driver -> driver.isActive()).findFirst().get().getDriverId());

			if (driverMaster.isPresent()) {

				DriverMaster driver = driverMaster.get();

				response.setDriverId(driver.getId());
				response.setDriverName(driver.getName());
				response.setDriverNumber(driver.getMobileNumber());

			}

			if (trip.getEmployeeId() != null) {
				response.setShipperName(shipperRepository.findById(trip.getEmployeeId()).get().getName());
			}

			response.setConsignment(
					trip.getConsignment().get(0).getGoodsType() + "/" + trip.getConsignment().get(0).getWeight());

			response.setGoodsType(trip.getConsignment().get(0).getGoodsType());

			response.setStartDateTime(trip.getStartedAt());

			responses.add(response);
		}

		return responses;
	}

	@Override
	@Transactional
	public TripResponse updateTrip(String tripId, TripRequest request, Map<String, String> headers,
			HttpHeaders httpHeaders) throws Exception {

		isValidRequest(headers);

		Optional<Trip> tripObj = repository.findById(tripId);
		if (tripObj.isEmpty()) {
			throw new Exception(TRIP_NOT_EXISTS);
		}

		Trip trip = tripObj.get();
		setLocationForTrip(trip, request, headers.get(TENANT_ID), httpHeaders);
		setDriver(trip, request);
		setVehicle(trip, request);
		setConsignment(trip, request);
		trip.setTransporterName(request.getTransporterName());

		repository.save(trip);

		// Deleting old imgLink In cases of updated Images
		imagesRepository.deleteByReferenceIdAndTypeAndIsDamaged(request.getDriver().getId(), DRIVER, false);
		setImages(trip.getDriver().get(0).getId(), request.getDriver().getImgs(), DRIVER);

		imagesRepository.deleteByReferenceIdAndTypeAndIsDamaged(request.getDriver().getId(), VEHICLE, false);
		setImages(trip.getVehicles().get(0).getId(), request.getVehicle().getImgs(), VEHICLE);

		return getTrip(tripId, headers, httpHeaders);
	}

	private void isValidRequest(Map<String, String> headers) throws Exception {

		if (!shipperRepository.existsById(headers.get(EMPLOYEE_ID))) {
			throw new Exception(ApplicationConstant.NO_SHIPPER_EXISTS);
		}

		if (!tenantRepository.existsById(headers.get(TENANT_ID))) {
			throw new Exception(ApplicationConstant.NO_TENANT_EXISTS);
		}

	}

	@Override
	public TripResponse getTripStatus(String tripId, Map<String, String> headers) throws Exception {

		isValidRequest(headers);

		Optional<Trip> tripObj = repository.findById(tripId);

		if (tripObj.isEmpty()) {
			throw new Exception(TRIP_NOT_EXISTS);
		}

		Trip trip = tripObj.get();

		TripResponse response = new TripResponse();
		response.setShipperName(shipperRepository.findById(headers.get(EMPLOYEE_ID)).get().getName());
		response.setCurrentStatus(trip.getCurrentStatus());
		response.setNextStatus(trip.getNextStatus());

		return response;
	}

	@Override
	public boolean deleteTrip(String tripId, Map<String, String> headers) throws Exception {
		isValidRequest(headers);

		Optional<Trip> tripObj = repository.findById(tripId);

		if (tripObj.isEmpty()) {
			throw new Exception(TRIP_NOT_EXISTS);
		}

		repository.delete(tripObj.get());

		return true;
	}

	@Transactional
	@Override
	public boolean addInventoryToTrip(TripInventory inventory, Map<String, String> headers) throws Exception {
		// TODO Auto-generated method stub

		isValidRequest(headers);

		Optional<Trip> tripObj = repository.findById(inventory.getTripId());

		if (tripObj.isEmpty()) {
			throw new Exception(TRIP_NOT_EXISTS);
		}

		Trip trip = tripObj.get();

		// Add GPS
		if (inventory.getGps() != null) {
			DeviceLog deviceLog = inventoryRepository.findByTenantIdAndTypeAndSerialNo(headers.get(TENANT_ID), GPS,
					inventory.getGps());

			if (deviceLog == null) {
				deviceLog = new DeviceLog();
			}

			DeviceInfo deviceInfo = deviceInfoRepository.findByTenantId(headers.get(TENANT_ID));
			if (deviceInfo == null) {
				throw new Exception("Register Tenant First");

			}

			deviceLog.setTenantId(headers.get(TENANT_ID));

			deviceLog.setSerialNo(inventory.getGps());
			deviceLog.setType(GPS);
			deviceLog.setDeviceInfo(deviceInfo);

			deviceLog.setSource(trip.getLocation().stream()
					.filter(location -> location.getActivity().equalsIgnoreCase(PICKUP)).findFirst().get().getId());

			deviceLog.setDestination(trip.getLocation().stream()
					.filter(location -> location.getActivity().equalsIgnoreCase(DROP)).findFirst().get().getId());
			deviceLog.setTrip(trip);
			deviceLog.setStatus(IN_USE);
			inventoryRepository.save(deviceLog);
		}

		trip.setTagCount(inventory.getTagCount());
		trip.setCurrentStatus(ApplicationConstant.INVENTORY_ADDED);
		trip.setNextStatus(SCAN_TAGS);

		repository.save(trip);

		return true;
	}

}
