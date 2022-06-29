package com.meratransport.trip.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class InventoryResponse {

	private String id;

	private List<String> gpsDeviceNumbder;

	private int gpsDeviceCount;

	private List<String> tagsNumber;

	private long tagCount;

	private String provider;

	private Date contractStartDate;

	private Date contractEndDate;

	private double price;

}
