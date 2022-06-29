package com.meratransport.trip.utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HttpConnection {

	public String multipartPost(final String url, final Map<String, Object> params, final Map<String, String> headers) {
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

	public String get(final String url, final Map<String, Object> params, final Map<String, String> headers) {
		String result = null;
		if (null == url || url.isBlank()) {
			throw new RuntimeException("URL cannot be empty.");
		}
		try {
//			Map<String, String> requestHeaders = prepareHeader();
//			if (null != headers) {
//				requestHeaders.putAll(headers);
//			}
			result = sendRequest(HttpMethod.GET, url, params, headers, false);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

//	public Map<String, String> prepareHeader() {
//		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("x-keeboot-tid", tid);
//		headers.put("x-keeboot-uid", uid);
//		headers.put("x-keeboot-gid", uid);
//		headers.put("x-keeboot-eid", eid);
//		headers.put("x-keeboot-apt", apt);
//		return headers;
//	}

	private String sendRequest(final HttpMethod httpMethod, String url, final Map<String, Object> params,
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

	private void prepareHeader(HttpHeaders httpHeaders, final Map<String, String> headers) {
		if (null != httpHeaders && null != headers && headers.size() > 0 && !headers.isEmpty()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpHeaders.add(entry.getKey(), entry.getValue());
			}
		}
	}

	private String prepareGetURL(final String url, final Map<String, Object> params) throws JsonProcessingException {
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

	private String prepareParams(final Map<String, Object> params, final boolean isJsonNeeded)
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

	public String post(final String url, final Map<String, Object> params) {
		String result = null;
		try {
			result = post(url, params, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String post(final String url, final Map<String, Object> params, final Map<String, String> headers) {
		String result = null;
		if (null == url || url.isBlank()) {
			throw new RuntimeException("URL cannot be empty.");
		}
		try {
//			Map<String, String> requestHeaders = prepareHeader();
//			if (null != headers) {
//				requestHeaders.putAll(headers);
//			}
			result = sendRequest(HttpMethod.POST, url, params, headers, false);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
