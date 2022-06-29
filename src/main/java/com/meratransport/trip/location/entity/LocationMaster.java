package com.meratransport.trip.location.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.geo.Point;

import lombok.Data;

@Data
@Entity
@Table(name="geo_locations")
public class LocationMaster {
	
	@Id
	@Column(name="location_id")
	private String locationId;
	
	private String name;
	private String address;
	@Column(name="location_code")
	private String locationCode;
	private String area;
	private String city;
	private String state;
	@Column(name="state_code")
	private String stateCode;
	private String zip;
	
	private String tenantId;
}
