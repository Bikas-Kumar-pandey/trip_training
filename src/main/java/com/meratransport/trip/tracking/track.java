package com.meratransport.trip.tracking;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class track {

	private static String authorisation = "";

	private static String simTrackingUrl = "https://india-agw.telenity.com/";
	private static String trackingurl = "http://services-int.staging.keeboot.com:9292/api/";

	private static String eid = "E65B4FB549F947DCBA20A429364388F1";
	private static String uid = "39D02360486E4B1EB754590A06538E12";
	private static String tid = "B70F2065FEBE48EEA24C091197D65A0C";
	private static String apt = "KEEBOOT-MAIN-WEB";

	public static void main(String[] args) {

		getauthKey();

//	sendConsent(getAuthToken(), "919870754417");

		getTracking(getAuthToken(), "919870754417", "0003018732254F1D851E24C090184370");

	}

	private static String getauthKey() {
		if (authorisation.equals("")) {
			StringBuilder auth = new StringBuilder();
			// auth.append(clientId);
			auth.append("keeboot30");
			auth.append(":");
			// auth.append(clientSecret);
			auth.append("Keeboot@2022");
			byte[] encodedAuth = Base64.encodeBase64(auth.toString().getBytes(StandardCharsets.UTF_8));
			authorisation = "Basic " + new String(encodedAuth);
		}
		return authorisation;
	}

	public static String getAuthToken() {
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", authorisation);
			String httpResponse = get("https://smarttrail.telenity.com/trail-rest/login", null, headers);
			if (!httpResponse.isBlank()) {
				JSONObject obj = new JSONObject(httpResponse);
				return (String) obj.get("token");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String multipartPost(final String url, final Map<String, Object> params,
			final Map<String, String> headers) {
		String result = null;
		if (null == url || url.isBlank()) {
			throw new RuntimeException("URL cannot be empty.");
		}
		try {
			result = sendRequest(HttpMethod.POST, url, params, headers, true);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Map<String, Object> sendConsent(String token, String mobileNumber) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("token", token);
		headers.put("host", simTrackingUrl);

		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> value = new ArrayList<>();
		Map<String, Object> el = new HashMap<String, Object>();

		el.put("firstName", "Ojasvi");
		el.put("lastName", "Bhardwaj");
		el.put("msisdn", "919870754417");
		value.add(el);
		params.put("entityImportList", value);

		String httpResponse = post("https://smarttrail.telenity.com/trail-rest/entities/import", params, headers);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(httpResponse, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String get(final String url, final Map<String, Object> params, final Map<String, String> headers) {
		String result = null;
		if (null == url || url.isBlank()) {
			throw new RuntimeException("URL cannot be empty.");
		}
		try {
			Map<String, String> requestHeaders = prepareHeader();
			if (null != headers) {
				requestHeaders.putAll(headers);
			}
			result = sendRequest(HttpMethod.GET, url, params, requestHeaders, false);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Map<String, String> prepareHeader() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("x-keeboot-tid", tid);
		headers.put("x-keeboot-uid", uid);
		headers.put("x-keeboot-gid", uid);
		headers.put("x-keeboot-eid", eid);
		headers.put("x-keeboot-apt", apt);
		return headers;
	}

	private static String sendRequest(final HttpMethod httpMethod, String url, final Map<String, Object> params,
			final Map<String, String> headers, final boolean isMultipart) throws IOException {
		String result = null;
		String body = null;
		boolean isGet = ("GET" == httpMethod.name()) ? true : false;
		if (isGet) {
			url = prepareGetURL(url, params);
		} else {
			body = prepareParams(params, true);
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		prepareHeader(httpHeaders, headers);
		if (isMultipart) {
			httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
		} else {
			httpHeaders.add("Content-Type", "application/json");
		}

		HttpEntity<String> requestEntity = new HttpEntity<>(body, httpHeaders);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, requestEntity, String.class);
		if (HttpStatus.OK == responseEntity.getStatusCode() || HttpStatus.CREATED == responseEntity.getStatusCode()) {
			result = responseEntity.getBody();
		}
		return result;
	}

	private static void prepareHeader(HttpHeaders httpHeaders, final Map<String, String> headers) {
		if (null != httpHeaders && null != headers && headers.size() > 0 && !headers.isEmpty()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpHeaders.add(entry.getKey(), entry.getValue());
			}
		}
	}

	private static String prepareGetURL(final String url, final Map<String, Object> params)
			throws JsonProcessingException {
		String result = url;
		if (null == url || url.isBlank()) {
			throw new RuntimeException("URL cannot be empty.");
		}
		if (null != params && !params.isEmpty()) {
			StringBuilder parameters = new StringBuilder(url).append("?");
			result = prepareParams(params, false);
			if (null == result || result.isBlank()) {
				result = parameters.toString();
			} else {
				result = parameters.append(result).toString();
			}
		}
		return result;
	}

	private static String prepareParams(final Map<String, Object> params, final boolean isJsonNeeded)
			throws JsonProcessingException {
		String result = null;
		if (null != params && !params.isEmpty() && params.size() > 0) {
			if (!isJsonNeeded) {
				StringBuilder parameters = new StringBuilder();
				for (Entry<String, Object> entry : params.entrySet()) {
					parameters.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
				}
				result = parameters.substring(0, parameters.length() - 1);
			} else {
				result = new ObjectMapper().writeValueAsString(params);
			}
		}
		return result;
	}

	public static Map<String, Object> getTracking(String token, String mobileNumber, String driverId) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("host", simTrackingUrl);
		headers.put("token", token);
		String url = "https://smarttrail.telenity.com/trail-rest/location/msisdnList/919870754417?lastResult=True";
		String httpResponse = get(url, null, headers);
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String,List<Map<String,Object>>> trackingDetails =   mapper.readValue(httpResponse, Map.class);
			response.put("tracking", trackingDetails);
			System.out.println(trackingDetails);
//			List<Map<String,Object>> res = trackingDetails.get("terminalLocation");
//			System.out.println(res);
			if(trackingDetails !=null && trackingDetails.get("terminalLocation").size()>0) {
				List<TelTracking> res = mapper.convertValue(trackingDetails.get("terminalLocation"), new TypeReference<List<TelTracking>>(){});
				double lat =res.get(0).getCurrentLocation().getLatitude();
				double	lng = res.get(0).getCurrentLocation().getLongtitude();
				UpdateTrackingDetails(driverId,lat ,lng);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private static void UpdateTrackingDetails(String driverId, double lat, double lng) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceid", driverId);
		params.put("lat", lat);
		params.put("lng", lng);
		params.put("deviceTime", getCurrentDate());
		String trackingResponse = post(trackingurl + "/tracking/trip/push_position_of_device", params);
		System.out.println(trackingResponse);
	}

	private static String getCurrentDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public static String post(final String url, final Map<String, Object> params) {
		String result = null;
		try {
			result = post(url, params, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String post(final String url, final Map<String, Object> params, final Map<String, String> headers) {
		String result = null;
		if (null == url || url.isBlank()) {
			throw new RuntimeException("URL cannot be empty.");
		}
		try {
			Map<String, String> requestHeaders = prepareHeader();
			if (null != headers) {
				requestHeaders.putAll(headers);
			}
			result = sendRequest(HttpMethod.POST, url, params, requestHeaders, false);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
