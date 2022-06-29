package com.meratransport.trip.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationRequest {

	private GeoLocation geoLocation;
	private String locationId;
	private String name;
	private String address;
	private String description;
	private String locationCode;
	private String area;
	private String city;
	private String state;
	private String zip;
	private boolean isGeoFenceActive;
	private String locationType;
	private boolean isActive;
}
