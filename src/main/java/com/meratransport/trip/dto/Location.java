package com.meratransport.trip.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Location {

	private String locationId;
	private String name;
	private String address;
	private String locationCode;
	private String area;
	private String city;
	private String state;
	private String zip;
	private GeoLocation geoLocation;
	private boolean isGeoFenceActive;
	private String locationType;
	private boolean isActive;

}
