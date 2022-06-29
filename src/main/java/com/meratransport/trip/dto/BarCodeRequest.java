package com.meratransport.trip.dto;

import java.util.List;

import lombok.Data;

@Data
public class BarCodeRequest {

	private String tripId;

	private String provider;

	private List<TagInfo> tagNumber;

}
