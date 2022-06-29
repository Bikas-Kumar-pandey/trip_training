package com.meratransport.trip.tracking.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meratransport.trip.constant.ApplicationConstant;
import com.meratransport.trip.dto.TrackingTrip;
import com.meratransport.trip.entity.Trip;
import com.meratransport.trip.entity.TripDriver;
import com.meratransport.trip.profile.entity.DriverMaster;
import com.meratransport.trip.profile.repository.ProfileDriverMasterRepository;
import com.meratransport.trip.repository.TripRepository;
import com.meratransport.trip.tracking.TelTracking;
import com.meratransport.trip.utility.HttpConnection;
import com.meratransport.trip.utility.Util;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class TrackingServiceImpl implements TrackingService {

	private String authorisation = "";

	@Value("${track.simTrackingUrl}")
	private String simTrackingUrl;

	@Value("${track.trackingurl}")
	private String trackingurl;

	@Value("${track.clientId}")
	private String clientId;

	@Value("${track.clientSecret}")
	private String clientSecret;

	@Autowired
	public TripRepository repository;

	@Autowired
	public HttpConnection httpConnection;

	@Autowired
	public ProfileDriverMasterRepository driverMasterRepository;

	private String getauthKey() {
		if (authorisation.equals("")) {
			StringBuilder auth = new StringBuilder();
			auth.append(clientId);
			auth.append(":");
			auth.append(clientSecret);
			byte[] encodedAuth = Base64.encodeBase64(auth.toString().getBytes(StandardCharsets.UTF_8));
			authorisation = "Basic " + new String(encodedAuth);
		}
		return authorisation;
	}

	public String getAuthToken() {
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", getauthKey());
			String httpResponse = httpConnection.get("https://smarttrail.telenity.com/trail-rest/login", null, headers);
			if (!httpResponse.isBlank()) {
				JSONObject obj = new JSONObject(httpResponse);
				return (String) obj.get("token");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, Object> sendConsent(Trip trip, Map<String, String> headers) throws Exception {

		List<TripDriver> drivers = trip.getDriver();
		for (TripDriver tripDriver : drivers) {
			if (tripDriver.isActive()) {
				Optional<DriverMaster> dr = driverMasterRepository.findById(tripDriver.getDriverId());
				if (dr.isEmpty()) {
					throw new Exception("Driver Not Found");
				}
				return sendConsent(dr.get().getMobileNumber(),
						dr.get().getName() != null ? dr.get().getName() : "KEEBOOT-NAME", "No LastName", headers);
			}

		}

		return null;

	}

	public Map<String, Object> sendConsent(String mobileNumber, String firstName, String lastName,
			Map<String, String> headers2) throws Exception {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("token", getAuthToken());
		headers.put("host", simTrackingUrl);
		headers.putAll(headers2);
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> value = new ArrayList<>();
		Map<String, Object> el = new HashMap<String, Object>();
		String mob = Util.validateMobile(mobileNumber);
		el.put("firstName", firstName);
		el.put("lastName", lastName);
		el.put("msisdn", mob);
		value.add(el);
		params.put("entityImportList", value);

		String httpResponse = httpConnection.post("https://smarttrail.telenity.com/trail-rest/entities/import", params,
				headers);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(httpResponse, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public Map<String, Object> getTracking(String tripId, Map<String, String> headers) throws Exception {

		Optional<Trip> trip = repository.findById(tripId);

		if (trip.isEmpty()) {
			throw new Exception("Trip Doesn't Exist");
		}
		List<TripDriver> drivers = trip.get().getDriver();
		for (TripDriver tripDriver : drivers) {
			if (tripDriver.isActive()) {
				Optional<DriverMaster> dr = driverMasterRepository.findById(tripDriver.getDriverId());
				if (dr.isEmpty()) {
					throw new Exception("Driver Not Found");
				}
				return getTracking(dr.get().getMobileNumber(), dr.get().getId(), headers);
			}

		}

		return null;
	}

	public Map<String, Object> getTracking(String mobileNumber, String driverId, Map<String, String> headers2) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("host", simTrackingUrl);
		headers.put("token", getAuthToken());
//		headers.putAll(headers2);
		String mob = Util.validateMobile(mobileNumber);
		String url = "https://smarttrail.telenity.com/trail-rest/location/msisdnList/" + mob + "?lastResult=True";
		String httpResponse = httpConnection.get(url, null, headers);
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, List<Map<String, Object>>> trackingDetails = mapper.readValue(httpResponse, Map.class);
			response.put("tracking", trackingDetails);
			System.out.println(trackingDetails);
			if (trackingDetails != null && trackingDetails.get("terminalLocation").size() > 0) {
				List<TelTracking> res = mapper.convertValue(trackingDetails.get("terminalLocation"),
						new TypeReference<List<TelTracking>>() {
						});
				double lat = res.get(0).getCurrentLocation().getLatitude();
				double lng = res.get(0).getCurrentLocation().getLongtitude();
				UpdateTrackingDetails(driverId, lat, lng, headers2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private void UpdateTrackingDetails(String driverId, double lat, double lng, Map<String, String> headers2) {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceid", driverId);
		params.put("lat", lat);
		params.put("lng", lng);
		params.put("deviceTime", getCurrentDate());
		String trackingResponse = httpConnection.post(trackingurl + "/tracking/trip/push_position_of_device", params,
				map);
		System.out.println(trackingResponse);
	}

	private String getCurrentDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public void registerDevice(String tripId, Map<String, String> headers) throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		Optional<Trip> trip = repository.findById(tripId);

		if (trip.isEmpty()) {
			throw new Exception("Trip Doesn't Exist");
		}
		List<TripDriver> drivers = trip.get().getDriver();
		for (TripDriver tripDriver : drivers) {
			if (tripDriver.isActive()) {
				Optional<DriverMaster> dr = driverMasterRepository.findById(tripDriver.getDriverId());
				if (dr.isEmpty()) {
					throw new Exception("Driver Not Found");
				}

				Map<String, String> extraDetails = new HashMap<String, String>();
				extraDetails.put("name", dr.get().getName());
				extraDetails.put("phone", dr.get().getMobileNumber());
				register("driver", dr.get().getId(), headers.get(ApplicationConstant.TENANT_ID), extraDetails);
			}

		}

	}

	public Map<String, Object> register(String categoryType, String uniqueId, String tenantId,
			Map<String, String> extraDetails) throws Exception {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", categoryType);
		params.put("groupname", tenantId);
		params.put("provider", "KEEBOOT");
		params.put("uniqueId", uniqueId);
		params.put("name", extraDetails.get("name"));
		params.put("phone", extraDetails.get("phone"));
		log.info(params.toString());
		String httpResponse = httpConnection.post(trackingurl + "/tracking/device", params);
		if (httpResponse != null && !httpResponse.isBlank()) {
			ObjectMapper objectMapper = new ObjectMapper();
			responseMap = objectMapper.readValue(httpResponse, Map.class);
			if (!responseMap.get("statusCode").equals(0)) {
				log.error(responseMap.toString());
				throw new Exception("Error in " + categoryType + " registration!" + responseMap.toString());
			}
		} else {
			log.error(httpResponse);
			throw new Exception("Error in " + categoryType + " registration!" + responseMap.toString());
		}
		return responseMap;
	}

	private HttpHeaders prepareHttpHeader(Map<String, String> headers) {

		HttpHeaders headers2 = new HttpHeaders();

		headers.forEach((key, value) -> headers2.set(key, value));

		return headers2;
	}

	@Override
	public void registerTrip(String tripId, Map<String, String> headers) throws Exception {

		TrackingTrip trackingTrip = new TrackingTrip();

		Optional<Trip> trip = repository.findById(tripId);

		if (trip.isEmpty()) {
			throw new Exception("Trip Doesn't Exist");
		}
		List<TripDriver> drivers = trip.get().getDriver();
		for (TripDriver tripDriver : drivers) {
			if (tripDriver.isActive()) {
				Optional<DriverMaster> dr = driverMasterRepository.findById(tripDriver.getDriverId());
				if (dr.isEmpty()) {
					throw new Exception("Driver Not Found");
				}

				trackingTrip.setTripid(tripId);
				trackingTrip.setGroupname(headers.get(ApplicationConstant.TENANT_ID));
				trackingTrip.setDeviceid(dr.get().getId());

			}

		}

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<TrackingTrip> post = new HttpEntity<TrackingTrip>(trackingTrip, prepareHttpHeader(headers));
		ResponseEntity<String> response = restTemplate.postForEntity(trackingurl + "/tracking/trip", post,
				String.class);
		log.info("registering the device  " + response.getBody());

	}

}
