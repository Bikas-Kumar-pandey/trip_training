package com.meratransport.trip.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class InventoryRequest {

	private List<String> gpsDeviceNumber;

	private List<String> tagsNumber;

	private String provider;

	private Date contractStartDate;

	private Date contractEndDate;

	private double price;

}
